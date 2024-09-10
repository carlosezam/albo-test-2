package com.ezam.rickandmorty.domain

interface Configuration {
    val idGenerator: IdGeneratorType
}

enum class IdGeneratorType {
    SEQUENTIAL,
    RANDOM;
}

