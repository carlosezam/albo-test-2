package com.ezam.rickandmorty.domain

// commonMain
interface NetworkStatus {
    suspend fun isConnected(): Boolean
}