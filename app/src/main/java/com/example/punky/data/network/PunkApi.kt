package com.example.punky.data.network

import com.example.punky.utils.logd
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PunkApi ( val client: HttpClient ): IPunkApi {

    private val apiUrl = "https://api.punkapi.com/v2/beers"

    override suspend fun getBeers(page: Int, perPage: Int) {
        val request = client.request<List<PunkBeer>> {
            method = HttpMethod.Get
            url( apiUrl )
            parameter("page", page)
            parameter("per_page", perPage)
        }

        //var beers = Json.decodeFromString<List<PunkBeer>>(request)
        logd( request.toString())
    }


}