package com.example.punky.data.network

import com.example.punky.utils.logd
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PunkApi @Inject constructor(val client: HttpClient ): IPunkApi {

    private val apiUrl = "https://api.punkapi.com/v2/beers"

    override suspend fun getBeers(page: Int, perPage: Int) : List<PunkBeer> {
        val result = client.request<List<PunkBeer>> {
            method = HttpMethod.Get
            url( apiUrl )
            parameter("page", page)
            parameter("per_page", perPage)
        }

        return result
    }


}