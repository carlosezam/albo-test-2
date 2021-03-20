package com.example.punky.data.local

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class ConvertersTest {

    @Test
    fun whenConverting_ListToString_shouldReturnJsonString() {
        val input = listOf("s1","s2","s3")
        val result = Converters.stringListToString( input )
        val jsonString = """["s1","s2","s3"]"""

        Assert.assertEquals(result, jsonString )
    }

    @Test
    fun whenConverting_EmptyListToString_shoulReturnEmptyArray(){

        val result = Converters.stringListToString( emptyList() )

        Assert.assertEquals( result, "[]" )
    }

    @Test
    fun whenConverting_BlankStringToList_shouldReturnEmptyList() {
        val input = "  "

        val result = Converters.stringToStringList(input)

        var expected = emptyList<String>()
        Assert.assertEquals( expected, result)
    }

    @Test
    fun whenConverting_JsonStringToList_shouldReturnList() {
        val input = """["s1","s2","s3"]"""

        val result = Converters.stringToStringList(input)

        var expected = listOf("s1","s2","s3")

        Assert.assertEquals( expected, result)
    }
}