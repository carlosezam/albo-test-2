package com.ezam.rickandmorty.data.remote

import android.content.res.Resources.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.ContentConvertException
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

interface RickandmortyApi {
    suspend fun getCharacters( page: Int = 1 ): Result<CharactersResult>
}



class RickandmortyApiRest( engine: HttpClientEngine ) : RickandmortyApi {

    private val client = HttpClient(engine){
        install(ContentNegotiation){
            val jsonConfig = Json {
                ignoreUnknownKeys = true
            }
            json(jsonConfig)
        }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

    }

    override suspend fun getCharacters(page: Int) : Result<CharactersResult> {

        return try{
            val response = client.get("https://rickandmortyapi.com/api/character?page=1")

            if( response.status == HttpStatusCode.NotFound){
                Result.failure(NotFoundException())
            } else {
                Result.success(response.body())
            }

        } catch ( e: IOException ){
            return Result.failure(e)
        } catch ( e: ContentConvertException ){
            return  Result.failure(e)
        }

    }
}