package com.punky.core.data

interface RemoteConfig {
    val isUpdated: Boolean
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getDouble(key: String): Double
    fun getLong(key: String): Long
    suspend fun fetch(): RemoteConfig
    suspend fun all() : Map<String,String>
}