package com.ezam.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezam.rickandmorty.data.LoadCharactersResult
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val characters: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val hasMorePages: Boolean = true
)

class CharacterListViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val uiState = MutableStateFlow(UiState())
    fun getUiState() = uiState.asStateFlow()

    private var currentPage: Int = 1

    fun loadCharacters() = viewModelScope.launch {

        if( uiState.value.hasMorePages.not() )
            return@launch

        val result = characterRepository.loadCharacters( currentPage )

        when(result){
            is LoadCharactersResult.RetryAgain -> {
                uiState.update { it.copy(hasError = true) }
            }
            is LoadCharactersResult.EndOfData -> {
                uiState.update { it.copy(hasMorePages = false) }
            }
            is LoadCharactersResult.Data -> {
                currentPage = result.next
                uiState.update { it.copy(characters = it.characters + result.characters) }
            }
        }
    }

}