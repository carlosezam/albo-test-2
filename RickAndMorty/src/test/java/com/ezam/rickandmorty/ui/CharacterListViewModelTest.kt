package com.ezam.rickandmorty.ui

import com.ezam.rickandmorty.data.LoadCharactersResult
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.domain.VitalStatus
import com.ezam.rickandmorty.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verifyOrder
import kotlin.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterListViewModelTest {

    @get:Rule
    val mockRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repository: CharacterRepository

    lateinit var viewModel: CharacterListViewModel

    @Before
    fun setup(){
        viewModel = CharacterListViewModel(repository)
    }

    @Test
    fun `hasError es true cuando el repositorio retorna error`(){

        // given
        coEvery { repository.loadCharacters(any()) } returns LoadCharactersResult.RetryAgain

        // when
        viewModel.loadCharacters()


        // then
        val state = viewModel.getUiState().value

        assert( state.hasError )
    }

    @Test
    fun `hasMorePAges es false cuando el repositorio retorna EndOfData`(){

        // given
        coEvery { repository.loadCharacters(any()) } returns LoadCharactersResult.EndOfData

        // when
        viewModel.loadCharacters()


        // then
        val state = viewModel.getUiState().value

        assertFalse( state.hasMorePages )
    }

    @Test
    fun `characters muestra los datos devueltos por el repositorio`(){

        val character1 = Character("1", "1", VitalStatus.Alive)
        val character2 = Character("2", "2", VitalStatus.Alive)
        // given
        coEvery { repository.loadCharacters(any()) }returns
                LoadCharactersResult.Data(characters = listOf(character1), next = 2) andThen
                LoadCharactersResult.Data(characters = listOf(character2), next = 3)

        // when
        viewModel.loadCharacters()
        viewModel.loadCharacters()


        // then
        val state = viewModel.getUiState().value

        assertContentEquals(listOf(character1, character2), state.characters)
    }

    @Test
    fun `invoca el repositorio con el page devuelto en el campo next`(){

        // given
        coEvery { repository.loadCharacters(any()) }returns
                LoadCharactersResult.Data(characters = emptyList(), next = 2) andThen
                LoadCharactersResult.Data(characters = emptyList(), next = 3)

        // when
        viewModel.loadCharacters()
        viewModel.loadCharacters()

        // then
        val state = viewModel.getUiState().value

        coVerifyOrder {
            repository.loadCharacters(1)
            repository.loadCharacters(2)
        }
    }

    @Test
    fun `no se invoca al repositorio despues de EndOfData`(){

        // given
        coEvery { repository.loadCharacters(any()) } returns
                LoadCharactersResult.Data(characters = emptyList(), next = 2) andThen
                LoadCharactersResult.EndOfData

        // when
        repeat(5) {
            viewModel.loadCharacters()
        }


        // then
        val state = viewModel.getUiState().value

        coVerify(exactly = 2) { repository.loadCharacters(any()) }
    }
 }