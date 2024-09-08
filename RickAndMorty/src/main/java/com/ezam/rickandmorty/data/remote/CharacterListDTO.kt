package com.ezam.rickandmorty.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CharacterListResult (
    val info: InfoDTO = InfoDTO(),
    val results: List<CharacterDTO> = emptyList(),
){

    @Serializable
    data class InfoDTO(
        val count: Int = 0,
        val pages: Int = 0,
        val next: String? = null,
        val prev: String? = null,
    )

    @Serializable
    data class CharacterDTO(
        val id: Int,
        val name: String,
        val image: String,
    )
}