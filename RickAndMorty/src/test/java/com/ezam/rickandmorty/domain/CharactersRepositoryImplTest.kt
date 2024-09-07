package com.ezam.rickandmorty.domain

import android.content.res.Resources.NotFoundException
import com.ezam.rickandmorty.data.CharactersRepositoryImpl
import com.ezam.rickandmorty.data.LoadCharactersResult
import com.ezam.rickandmorty.data.remote.CharactersResult
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.ConnectException

private val json = Json { ignoreUnknownKeys = true }

class CharactersRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var api: RickandmortyApi

    lateinit var repository: CharactersRepositoryImpl

    @Before
    fun setup(){
        repository = CharactersRepositoryImpl(api)
    }

    @Test
    fun `result existoso cuando api responde ok`() = runBlocking {

        // given
        val charactersResult = json.decodeFromString<CharactersResult>(allCharacters)

        coEvery { api.getCharacters(any()) } returns Result.success(charactersResult)

        // when
        val result = repository.loadCharacters(2)

        // then
        assertTrue( result is LoadCharactersResult.Data )
        assertEquals( 2, (result as LoadCharactersResult.Data).characters.size )
        assertEquals( 3, result.next )
    }

    @Test
    fun `retorna EndOfData cuando el api devuelve un notFound`(): Unit = runBlocking {
        // given

        coEvery { api.getCharacters(any()) } returns Result.failure(NotFoundException())

        // when
        val result = repository.loadCharacters(2)

        // then
        assertTrue( result is LoadCharactersResult.EndOfData )
    }

    @Test
    fun `retorna RetryAgain cuando el api devuelve un error desconocido`(): Unit = runBlocking {
        // given

        coEvery { api.getCharacters(any()) } returns Result.failure(ConnectException())

        // when
        val result = repository.loadCharacters(2)

        // then
        assertTrue( result is LoadCharactersResult.RetryAgain )
    }
}


val allCharacters = """
    {
        "info": {
            "count": 826,
            "pages": 42,
            "next": "https://rickandmortyapi.com/api/character?page=2",
            "prev": null
        },
        "results": [
            {
                "id": 1,
                "name": "Rick Sanchez",
                "status": "Alive",
                "species": "Human",
                "type": "",
                "gender": "Male",
                "origin": {
                    "name": "Earth (C-137)",
                    "url": "https://rickandmortyapi.com/api/location/1"
                },
                "location": {
                    "name": "Citadel of Ricks",
                    "url": "https://rickandmortyapi.com/api/location/3"
                },
                "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                "episode": [
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                    "https://rickandmortyapi.com/api/episode/3"
                ],
                "url": "https://rickandmortyapi.com/api/character/1",
                "created": "2017-11-04T18:48:46.250Z"
            },
            {
                "id": 2,
                "name": "Morty Smith",
                "status": "Alive",
                "species": "Human",
                "type": "",
                "gender": "Male",
                "origin": {
                    "name": "unknown",
                    "url": ""
                },
                "location": {
                    "name": "Citadel of Ricks",
                    "url": "https://rickandmortyapi.com/api/location/3"
                },
                "image": "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                "episode": [
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                    "https://rickandmortyapi.com/api/episode/3"
                ],
                "url": "https://rickandmortyapi.com/api/character/2",
                "created": "2017-11-04T18:50:21.651Z"
            }
        ]
    }
""".trimIndent()


