package com.ezam.rickandmorty.di

import com.ezam.rickandmorty.utils.RandomIdGenerator
import com.ezam.rickandmorty.data.CharactersRepositoryImpl
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import com.ezam.rickandmorty.data.remote.RickandmortyApiRest
import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.domain.Configuration
import com.ezam.rickandmorty.domain.IdGenerator
import com.ezam.rickandmorty.domain.IdGeneratorType
import com.ezam.rickandmorty.data.local.RemoteConfiguration
import com.ezam.rickandmorty.utils.SequentialIdGenerator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

@Module
@InstallIn(SingletonComponent::class)
interface CharacterRepositoryModule {

    @Binds
    fun bindsCharacterRepository( impl: CharactersRepositoryImpl ) : CharacterRepository

    @Binds
    fun bindsRickandmortyApi( impl: RickandmortyApiRest ) : RickandmortyApi

    @Binds
    fun bindsConfiguration(impl: RemoteConfiguration) : Configuration

    companion object {

        @Provides
        fun bindsIdGenerator(configuration: Configuration) : IdGenerator {
            return when (configuration.idGenerator){
                IdGeneratorType.SEQUENTIAL -> SequentialIdGenerator(1..826)
                IdGeneratorType.RANDOM -> RandomIdGenerator(1..826)
            }
        }

        @Provides
        fun providesHttpClientEngine() : HttpClientEngine {
            return OkHttp.create()
        }
    }
}