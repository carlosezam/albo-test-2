package com.ezam.rickandmorty.domain

import com.ezam.rickandmorty.data.LoadCharactersResult
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun loadCharacters( page: Int = 1) : LoadCharactersResult
    suspend fun loadCharacter( id: Int = 1) : Flow<Character?>
    suspend fun randomCharacter() : Flow<Character?>
}

fun interface IdGenerator{
    fun nextId(): Int
}