package com.example.punky.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InAppUpdateModule {
    @Provides
    fun providesUpdateManager(@ApplicationContext context: Context): AppUpdateManager {
        return AppUpdateManagerFactory.create(context)
    }
}