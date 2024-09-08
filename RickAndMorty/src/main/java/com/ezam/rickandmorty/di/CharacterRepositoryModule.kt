package com.ezam.rickandmorty.di

import com.ezam.rickandmorty.RandomIdGenerator
import com.ezam.rickandmorty.data.CharactersRepositoryImpl
import com.ezam.rickandmorty.data.remote.RickandmortyApi
import com.ezam.rickandmorty.data.remote.RickandmortyApiRest
import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.domain.IdGenerator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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

    companion object {

        @Provides
        fun bindsIdGenerator() : IdGenerator {
            return RandomIdGenerator(0..827)
        }

        @Provides
        fun providesHttpClientEngine() : HttpClientEngine {
            return OkHttp.create()
        }
    }
}