package com.ezam.rickandmorty.domain

import com.ezam.rickandmorty.data.LoadCharactersResult

interface CharacterRepository {
    suspend fun loadCharacters( page: Int = 1) : LoadCharactersResult
    suspend fun loadCharacter( id: Int = 1) : Character?
    suspend fun randomCharacter() : Character?
}

interface IdGenerator{
    fun nextId(): Int
}