package com.ezam.rickandmorty.domain

import com.ezam.rickandmorty.data.LoadCharactersResult

interface CharacterRepository {
    suspend fun loadCharacters( page: Int = 1) : LoadCharactersResult
}