package com.example.punky.ui.beerdeatils


data class UiBeerDetails(
    val name: String,
    val imageUrl: String?,
    val tagline: String,
    val description: String,
    val firstBrewed: String,
    val foodPairing: List<String>
)
