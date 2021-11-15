package com.example.punky.di

import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
class ApplicationModule {

    @Provides
    fun providesKtorHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(JsonFeature){
                val json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                }
                serializer = KotlinxSerializer(json)
            }
            engine {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
                addInterceptor( loggingInterceptor )
            }
        }
    }
}