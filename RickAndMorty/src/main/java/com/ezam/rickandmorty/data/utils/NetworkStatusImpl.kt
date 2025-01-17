package com.ezam.rickandmorty.data.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ezam.rickandmorty.domain.NetworkStatus
import com.punky.core.utils.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NetworkStatusImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val dispatcherProvider: DispatcherProvider
) : NetworkStatus {
    override suspend fun isConnected(): Boolean = withContext(dispatcherProvider.io()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }
}