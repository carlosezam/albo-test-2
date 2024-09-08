package com.ezam.rickandmorty

import com.ezam.rickandmorty.domain.IdGenerator
import kotlin.random.Random
import kotlin.random.nextInt

class RandomIdGenerator(
    private val range: IntRange
) : IdGenerator
{

    override fun nextId(): Int {
        return Random.nextInt(range)
    }
}