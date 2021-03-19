package com.example.punky.data.network

interface IPunkApi {
    suspend fun getBeers( page: Int, perPage: Int)
}