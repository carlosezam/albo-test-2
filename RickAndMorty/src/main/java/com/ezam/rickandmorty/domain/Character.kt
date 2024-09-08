package com.ezam.rickandmorty.domain

data class Character(
    val name: String,
    val image: ByteArray,
    val status: VitalStatus
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Character

        if (name != other.name) return false
        if (!image.contentEquals(other.image)) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}

enum class VitalStatus {
    Alive,
    Dead,
    Unknown;

    companion object{
        fun fromString(value: String) = VitalStatus.entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: Unknown
    }
}