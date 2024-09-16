package com.ezam.rickandmorty.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import com.ezam.rickandmorty.data.utils.NetworkStatusImpl
import com.ezam.rickandmorty.domain.NetworkStatus
import com.punky.core.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkStatusModule {

    @Provides
    fun providesNetworkStatus(@ApplicationContext context: Context, dispatcherProvider: DispatcherProvider): NetworkStatus {
        val connectivityManager =
            ContextCompat.getSystemService(context, ConnectivityManager::class.java)
                ?: throw RuntimeException("No es posible instanciar ConnectivityManager")
        return NetworkStatusImpl(connectivityManager, dispatcherProvider)
    }
}