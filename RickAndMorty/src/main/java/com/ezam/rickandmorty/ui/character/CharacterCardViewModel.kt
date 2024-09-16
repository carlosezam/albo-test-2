package com.ezam.rickandmorty.ui.character

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.ezam.rickandmorty.data.byteArrayToBitmap
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
class CharacterCardViewModel @Inject constructor(
    private val repository: CharacterRepository,
): ViewModel() {

    private val state = MutableStateFlow<CharacterCardState?>(null)
    fun getState() = state.asStateFlow()

    fun nexRandom() = viewModelScope.launch {
        repository.randomCharacter()?.let { next ->

            val image = byteArrayToBitmap(next.image) ?: return@let

            val palette = generatePalette(image)

            val new = CharacterCardState(
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

    suspend fun generatePalette(from: Bitmap): Palette {
        return withContext(Dispatchers.IO){
            Palette.from(from).generate()
        }
    }
}