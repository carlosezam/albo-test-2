package com.example.punky.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_beer_keys")
data class RemoteBeerKeys(
    @PrimaryKey
    val beerId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
