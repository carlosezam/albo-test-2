package com.ezam.rickandmorty.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.ezam.rickandmorty.graphql.GetCharacterQuery
import javax.inject.Inject

class RickandmortyApolloApi @Inject constructor(
    private val apolloClient: ApolloClient
) : RickandmortyApi {

    override suspend fun getCharacters(page: Int): Result<CharacterListResult> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): Result<CharacterItemDTO> {
        return try {
            val response = apolloClient.query(GetCharacterQuery(id.toString())).execute()

            if (response.hasErrors()) {
                return Result.failure(IllegalStateException("Apollo Error"))
            }
            val character = response.data?.character

            if (character?.id == null) {
                return Result.failure(IllegalStateException("Character id is null"))
            }

            val dto = CharacterItemDTO(
                id = character.id.toInt(),
                name = character.name.orEmpty(),
                image = character.image.orEmpty(),
                status = character.status.orEmpty()
            )
            Result.success(dto)
        } catch (e: ApolloException) {
            Result.failure(e)
        }
    }
}