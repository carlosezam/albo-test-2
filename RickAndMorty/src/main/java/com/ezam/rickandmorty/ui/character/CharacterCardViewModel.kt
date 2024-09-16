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
import com.punky.core.utils.DispatcherProvider
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
    private val dispatcher: DispatcherProvider,
): ViewModel() {

    private val state = MutableStateFlow<CharacterCardState?>(null)
    fun getState() = state.asStateFlow()

    private var lastPrimaryColor: Color? = null
    private var lastTextColor: Color? = null

    fun nexRandom() = viewModelScope.launch {
        repository.randomCharacter().collect{ next ->

            if( next == null) return@collect

            val image = byteArrayToBitmap(next.image)

            val palette = image?.let { generatePalette(it) }

            palette?.dominantSwatch?.let {
                lastPrimaryColor = Color(it.rgb)
                lastTextColor = Color(it.bodyTextColor)
            }

            val new = CharacterCardState(
                isLoading = false,
                name = next.name,
                status = next.status,
                image = image?.asImageBitmap(),
                primaryColor = lastPrimaryColor ?: Color.Gray,
                textColor = lastTextColor ?: Color.White
            )

            state.update { new }
        }
    }

    private suspend fun generatePalette(from: Bitmap): Palette {
        return withContext(dispatcher.io()){
            Palette.from(from).generate()
        }
    }
}