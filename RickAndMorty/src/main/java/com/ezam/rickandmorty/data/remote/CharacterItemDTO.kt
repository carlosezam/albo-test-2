package com.ezam.rickandmorty.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CharacterItemDTO (
    val id: Int,
    val name: String,
    val image: String,
)