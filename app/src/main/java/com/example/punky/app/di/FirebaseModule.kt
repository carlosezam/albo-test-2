package com.example.punky.app.di

import com.example.punky.app.data.GoogleFirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.punky.core.data.RemoteConfig
import com.punky.core.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MobileServiceModule {

    companion object {
        @Provides
        fun providesRemoteConfig(dispatcherProvider: DispatcherProvider) : RemoteConfig {
            return GoogleFirebaseRemoteConfig(dispatcherProvider, FirebaseRemoteConfig.getInstance())
        }

    }
}