package com.ezam.rickandmorty.data.remote

interface RickandmortyApi {
    suspend fun getCharacters(page: Int = 1): Result<CharacterListResult>
    suspend fun getCharacter(id: Int = 1): Result<CharacterItemDTO>
}