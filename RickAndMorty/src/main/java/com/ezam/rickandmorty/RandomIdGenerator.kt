package com.ezam.rickandmorty

import com.ezam.rickandmorty.domain.IdGenerator
import kotlin.random.Random
import kotlin.random.nextInt

class RandomIdGenerator  constructor(
    private val range: IntRange
) : IdGenerator
{

    override fun nextId(): Int {
        return Random.nextInt(range)
    }
}