package com.punky.core.utils

inline fun <reified T : Enum<T>> enumFromName(name: String): T? {
    return enumValues<T>().firstOrNull { it.name.equals(name, true) }
}