package com.example.punky.data.network

import kotlinx.serialization.Serializable

@Serializable
data class PunkBeer(
        val name: String,
        val image_url: String?,
        val tagline: String,
        val description: String,
        val first_brewed: String,
        val food_pairing: List<String>
)
