package com.example.punky.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "beers"
)
data class Beer(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "tagline") val tagline: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "first_brewed") val first_brewed: String,
    @ColumnInfo(name = "food_pairing") val food_pairing: List<String>
)
