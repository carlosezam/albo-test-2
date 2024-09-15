package com.ezam.rickandmorty.data.remote

import android.content.res.Resources.NotFoundException
import com.google.common.truth.Truth
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException

class RickandmortyRestApiTest{

    @Test
    fun `prev es null, cuando se consulta la primera pagina`(): Unit = runBlocking {

        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(firstPageCharacters),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RickandmortyRestApi( mockEngine ).getCharacters().getOrThrow()

        assertNull(api.info.prev)
    }

    @Test
    fun `next es null, cuando se consulta la primera pagina`(): Unit = runBlocking {

        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(lastPageCharacters),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RickandmortyRestApi( mockEngine ).getCharacters().getOrThrow()

        assertNull(api.info.next)
    }

    @Test
    fun `result contiene los items`(): Unit = runBlocking {

        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(allChracters),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val response = RickandmortyRestApi( mockEngine ).getCharacters().getOrThrow()

        assertEquals(2, response.results.size)

        val first = response.results.first()

        assertEquals( 1, first.id)
        assertEquals( "Rick Sanchez", first.name)

    }

    @Test
    fun `retorna failure cuando no hay mas paginas`(): Unit =  runBlocking {

        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(nothingThere),
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val response = RickandmortyRestApi( mockEngine ).getCharacters().exceptionOrNull()

        assertNotNull( response )
        assertTrue( response is NotFoundException ) //UnknownHostException
    }

    @Test
    fun `retorna failure cuando hay un error de red`(): Unit = runBlocking {
        val mockEngine = MockEngine {
            throw ConnectException()
        }

        val response =  RickandmortyRestApi( mockEngine ).getCharacters().exceptionOrNull()

        assertNotNull(response)
        assertTrue( response is ConnectException )
    }

    @Test
    fun `retorna failure cuando hay un gateway timeout`(): Unit = runBlocking {
        val mockEngine = MockEngine {
            respond(
                content = ByteArray(0),
                status = HttpStatusCode.GatewayTimeout,
                headers = headersOf()
            )
        }

        val response =  RickandmortyRestApi( mockEngine ).getCharacters().exceptionOrNull()

        Truth.assertThat(response).isNotNull()
    }

    @Test
    fun `retorna failure cuando no hay un json inválido`(): Unit =  runBlocking {

        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel("invalid json"),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val response = RickandmortyRestApi( mockEngine ).getCharacters().exceptionOrNull()

        assertNotNull( response )
        assertTrue( response !is NotFoundException ) //UnknownHostException
    }

    @Test
    fun `getCharacter retorna exitoso`(): Unit = runBlocking {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(character1),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val response = RickandmortyRestApi(mockEngine).getCharacter(1).getOrThrow()

        assertEquals(1, response.id)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", response.image)
        assertEquals("Rick Sanchez", response.name)
        assertEquals("Alive", response.status)
    }
}

val firstPageCharacters = """
{
    "info": {
        "count": 826,
        "pages": 42,
        "next": "https://rickandmortyapi.com/api/character?page=2",
        "prev": null
    },
    "results": []
}
""".trimIndent()

val lastPageCharacters = """
{
    "info": {
        "count": 826,
        "pages": 42,
        "next": null,
        "prev": "https://rickandmortyapi.com/api/character?page=2"
    },
    "results": []
}
""".trimIndent()

// 404
val nothingThere = """
{
    "error": "There is nothing here"
}
""".trimIndent()


val allChracters = """
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


val character1 = """
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
    }
""".trimIndent()