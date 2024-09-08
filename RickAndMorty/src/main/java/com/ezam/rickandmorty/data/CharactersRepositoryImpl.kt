package com.ezam.rickandmorty.data

import android.content.res.Resources.NotFoundException
import com.ezam.rickandmorty.data.remote.CharacterItemDTO
import com.ezam.rickandmorty.data.remote.CharacterListResult
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.domain.IdGenerator
import javax.inject.Inject


sealed interface LoadCharactersResult {
    data object EndOfData: LoadCharactersResult
    data object RetryAgain: LoadCharactersResult
    data class Data(val characters: List<Character>, val next: Int): LoadCharactersResult
}

class CharactersRepositoryImpl @Inject constructor(
    private val api: RickandmortyApi,
    private val idGenerator: IdGenerator,
) : CharacterRepository {

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

    override suspend fun loadCharacter(id: Int): Character? {
        return api.getCharacter(id).getOrNull()?.toCharacter()
    }

    override suspend fun randomCharacter(): Character? {
        return api.getCharacter( idGenerator.nextId() ).getOrNull()?.toCharacter()
    }
}

fun CharacterListResult.CharacterDTO.toCharacter() = Character(name = name, imageUrl = image)
fun CharacterItemDTO.toCharacter() = Character(name = name, imageUrl = image)