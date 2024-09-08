package com.ezam.rickandmorty.ui.character

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import com.ezam.rickandmorty.domain.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CharacterItemViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: CharacterRepository,
): ViewModel() {

    private val state = MutableStateFlow<CharacterItemState?>(null)
    fun getState() = state.asStateFlow()

    fun nexRandom() = viewModelScope.launch {
        repository.randomCharacter()?.let { next ->

            val image = downloadBitmap(next.imageUrl) ?: return@let

            val palette = generatePalette(image)

            val new = CharacterItemState(
                isLoading = false,
                name = next.name,
                status = next.status,
                image = image.asImageBitmap(),
                primaryColor = palette.dominantSwatch?.let { Color(it.rgb) } ?: Color.Gray,
                textColor = palette.dominantSwatch?.bodyTextColor?.let { Color(it) } ?: Color.White
            )

            state.update { new }
        }
    }

    suspend fun downloadBitmap(imageUrl: String) : Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = context.imageLoader.execute(request)

        return (result.drawable as? BitmapDrawable)?.bitmap
    }

    suspend fun generatePalette(from: Bitmap): Palette {
        return withContext(Dispatchers.IO){
            Palette.from(from).generate()
        }
    }
}