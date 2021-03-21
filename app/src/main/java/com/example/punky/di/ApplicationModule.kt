package com.example.punky.di

import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

@Module
class ApplicationModule {

    @Provides
    fun providesKtorHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(JsonFeature){
                val json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
                serializer = KotlinxSerializer(json)
            }
        }
    }
}