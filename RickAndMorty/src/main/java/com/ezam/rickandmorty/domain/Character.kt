package com.ezam.rickandmorty.domain

data class Character(
    val name: String,
    val imageUrl: String,
    val status: VitalStatus
)

enum class VitalStatus {
    Alive,
    Dead,
    Unknown;

    companion object{
        fun fromString(value: String) = VitalStatus.entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: Unknown
    }
}