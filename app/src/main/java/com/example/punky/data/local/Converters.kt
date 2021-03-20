package com.example.punky.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Converters {


    @TypeConverter
    @JvmStatic
    fun stringListToString( input: List<String> ) : String {
        return Json.encodeToString( input )
    }

    @TypeConverter
    @JvmStatic
    fun stringToStringList( input: String ) : List<String>
        = if ( input.isBlank()) emptyList() else Json.decodeFromString( input)
}