package com.ezam.rickandmorty.data

import android.content.res.Resources.NotFoundException
import com.ezam.rickandmorty.data.local.CharacterDao
import com.ezam.rickandmorty.data.local.CharacterEntity
import com.ezam.rickandmorty.data.remote.CharacterItemDTO
import com.ezam.rickandmorty.data.remote.CharacterListResult
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.domain.IdGenerator
import com.ezam.rickandmorty.domain.ImageDownloader
import com.ezam.rickandmorty.domain.VitalStatus
import com.punky.core.utils.enumFromName
import javax.inject.Inject


sealed interface LoadCharactersResult {
    data object EndOfData: LoadCharactersResult
    data object RetryAgain: LoadCharactersResult
    data class Data(val characters: List<Character>, val next: Int): LoadCharactersResult
}

class CharactersRepositoryImpl @Inject constructor(
    private val api: RickandmortyApi,
    private val idGenerator: IdGenerator,
    private val imageDownloader: ImageDownloader,
    private val characterDao: CharacterDao
) : CharacterRepository {

    // Variable para almacenar el último ID no procesado
    private var lastFailedId: Int? = null

    override suspend fun loadCharacters(page: Int) : LoadCharactersResult {

        val local = characterDao.getPage(page)
        if(local?.size == 20)
            return LoadCharactersResult.Data( characters = local.map { it.toCharacter() }, next = page + 1 )

        val remote = api.getCharacters(page)

        val error = remote.exceptionOrNull()
        when{
            error is NotFoundException -> return LoadCharactersResult.EndOfData
            error != null -> return LoadCharactersResult.RetryAgain
        }

        val charactersWithImage = remote.getOrThrow().results.map {
            val image = imageDownloader.downloadImageAsByteArray(it.image) ?: ByteArray(0)
            it.toCharacterEntity(image)
        }

        characterDao.upsert(charactersWithImage)

        val newCharacters = characterDao.getPage(page)?.map { it.toCharacter() } ?: emptyList()
        return LoadCharactersResult.Data(characters = newCharacters, next = page + 1)
    }

    override suspend fun loadCharacter(id: Int): Character? {
        val local = characterDao.getById(id)
        if( local != null )
            return local.toCharacter()

        val remote = api.getCharacter(id).getOrNull() ?: return null

        val image = imageDownloader.downloadImageAsByteArray(remote.image) ?: ByteArray(0)

        characterDao.upsert( remote.toCharacterEntity(image) )

        return characterDao.getById(id)?.toCharacter()
    }

    override suspend fun randomCharacter(): Character? {
        // Si hay un ID fallido, lo volvemos a intentar, de lo contrario generamos uno nuevo
        val idToTry = lastFailedId ?: idGenerator.nextId()

        // Intentamos cargar el personaje
        val character = loadCharacter(idToTry)

        // Si falla (es null), guardamos este ID para reintentarlo la próxima vez
        if (character == null) {
            lastFailedId = idToTry
        } else {
            // Si es exitoso, limpiamos el lastFailedId
            lastFailedId = null
        }

        return character
    }
}

fun CharacterEntity.toCharacter() = Character(
    name = name,
    image = image,
    status = enumFromName<VitalStatus>(status) ?: VitalStatus.Unknown
)

fun CharacterItemDTO.toCharacterEntity(downloadedImage: ByteArray) = CharacterEntity(
    id = id,
    name = name,
    status = status,
    image = downloadedImage
)
fun CharacterListResult.CharacterDTO.toCharacterEntity(downloadedImage: ByteArray) = CharacterEntity(
    id = id,
    name = name,
    status = status,
    image = downloadedImage,
)