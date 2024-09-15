package com.ezam.rickandmorty.domain

import android.content.res.Resources.NotFoundException
import com.ezam.rickandmorty.data.CharactersRepositoryImpl
import com.ezam.rickandmorty.data.local.CharacterDao
import com.ezam.rickandmorty.data.local.CharacterEntity
import com.ezam.rickandmorty.data.remote.CharacterItemDTO
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import com.ezam.rickandmorty.data.toCharacter
import com.google.common.truth.Truth.assertThat
import io.mockk.Called
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val json = Json { ignoreUnknownKeys = true }

class CharactersRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    // Mocks
    private val characterDao: CharacterDao = mockk()
    private val api: RickandmortyApi = mockk()
    private val imageDownloader: ImageDownloader = mockk()
    private val idGenerator: IdGenerator = mockk()

    // Repositorio a testear
    private lateinit var characterRepository: CharacterRepository


    @Before
    fun setup(){
        characterRepository = CharactersRepositoryImpl(api, idGenerator, imageDownloader, characterDao)
    }

    @Test
    fun `debe retornar el character local si existe`(): Unit = runBlocking {
        // Arrange: Simulamos que el personaje local existe en la base de datos
        val localCharacterEntity = CharacterEntity(1, "offline", "dead", ByteArray(0))

        coEvery { characterDao.getById(1) } returns localCharacterEntity

        // Act: Llamamos al metodo
        val result = characterRepository.loadCharacter(1)

        // Assert: Verificamos que se devuelve el character local
        assertThat(result).isEqualTo(localCharacterEntity.toCharacter())

        // Verificamos que la API y el imageDownloader no fueron llamados
        verify { api wasNot Called }
        verify { imageDownloader wasNot Called }
    }

    @Test
    fun `debe retorar null si no se ecuentra el character ni en local ni remoto`(): Unit = runBlocking {
        // Arrange: Simulamos que el personaje no está ni en la base de datos ni remotamente
        coEvery { characterDao.getById(1) } returns null
        coEvery { api.getCharacter(1) } returns Result.failure(NotFoundException())

        // Act: Llamamos al método
        val result = characterRepository.loadCharacter(1)

        // Assert: Verificamos que el resultado es null
        assertThat(result).isNull()
    }

    @Test
    fun `debe guardar el character con la imagen cuando solo se encuentra en remoto`(): Unit = runBlocking {
        // Arrange: Simulamos que no hay personaje local pero que existe remotamente

        // Stub del item remoto
        val remoteCharacter = CharacterItemDTO(1, "online", "online_image_url", "alive")

        // Stub del resultado esperado
        val characterEntity = CharacterEntity(1, "online", "alive", byteArrayOf(1, 2, 3))

        coEvery { characterDao.getById(1) } returns null andThen characterEntity
        coEvery { api.getCharacter(1) } returns Result.success(remoteCharacter)

        coEvery { imageDownloader.downloadImageAsByteArray("online_image_url") } returns byteArrayOf(1, 2, 3)
        coEvery { characterDao.upsert(characterEntity) } just Runs

        // Act: Llamamos al método
        val result = characterRepository.loadCharacter(1)

        // Assert: Verificamos que el personaje se guardó y se devolvió correctamente
        assertThat(result).isEqualTo(characterEntity.toCharacter())
        coVerify { characterDao.upsert(characterEntity) }
    }

    @Test
    fun `debe llamar a loadCharacter con id generado random`(): Unit = runBlocking {
        // Arrange: Creamos un spy del repository para poder verificar llamadas internas
        val spyRepository = spyk(characterRepository)

        val randomId = 42
        val expectedCharacter = Character("Rick", byteArrayOf(1, 2, 3), VitalStatus.Alive)

        every { idGenerator.nextId() } returns randomId
        coEvery { spyRepository.loadCharacter(randomId) } returns expectedCharacter

        // Act: Llamamos a randomCharacter
        val result = spyRepository.randomCharacter()

        // Assert: Verificamos que se llamó a loadCharacter con el ID generado
        coVerify { spyRepository.loadCharacter(randomId) }

        // Verificamos que el resultado sea el esperado
        assertThat(result).isEqualTo(expectedCharacter)
    }

    @Test
    fun `debe reintentar con el mismo id si loadCharacter falla`(): Unit = runBlocking {
        // Arrange: Creamos un spy del repository para poder verificar llamadas internas
        val spyRepository = spyk(characterRepository)

        val failedId = 42
        val validCharacter = Character("Rick", byteArrayOf(1, 2, 3), VitalStatus.Alive )

        // Simulamos que se genera el ID que falla la primera vez y es exitoso la segunda
        every { idGenerator.nextId() } returns failedId andThen failedId.plus(1)
        coEvery { spyRepository.loadCharacter(failedId) } returns null andThen validCharacter

        // Act: Llamamos a randomCharacter por primera vez (falla)
        var result = spyRepository.randomCharacter()

        // Assert: Verificamos que no se devuelve un personaje
        assertThat(result).isNull()

        // Act: Llamamos a randomCharacter nuevamente (esta vez debería ser exitoso)
        result = spyRepository.randomCharacter()

        // Assert: Verificamos que ahora devuelve el personaje correcto
        assertThat(result).isEqualTo(validCharacter)

        // Verificamos que se usó el mismo ID ambas veces
        coVerify(exactly = 2) { spyRepository.loadCharacter(failedId) }
    }
}

val character = """
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


