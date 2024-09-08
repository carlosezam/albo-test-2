package com.example.punky.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun providesKtorHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation){
                val jsonConfig = Json {
                    ignoreUnknownKeys = true
                }
                json(jsonConfig)
            }
            engine {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                addInterceptor( loggingInterceptor )
            }
        }
    }
}