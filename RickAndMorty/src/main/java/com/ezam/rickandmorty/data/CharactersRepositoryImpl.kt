package com.ezam.rickandmorty.data

import android.content.res.Resources.NotFoundException
import com.ezam.rickandmorty.data.remote.CharactersResult
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.CharacterRepository


sealed interface LoadCharactersResult {
    data object EndOfData: LoadCharactersResult
    data object RetryAgain: LoadCharactersResult
    data class Data(val characters: List<Character>, val next: Int): LoadCharactersResult
}

class CharactersRepositoryImpl (private val api: RickandmortyApi) : CharacterRepository {

    override suspend fun loadCharacters(page: Int) : LoadCharactersResult {

        val result = api.getCharacters(page)

        result.onSuccess {
            val characters = it.results.map{ it.toCharacter() }
            return LoadCharactersResult.Data(characters = characters, next = page + 1)
        }


        val error = result.exceptionOrNull()

        if(error is NotFoundException)
            return LoadCharactersResult.EndOfData

        return LoadCharactersResult.RetryAgain
    }

}

fun CharactersResult.CharacterDTO.toCharacter() = Character(name = name, imageUrl = image)