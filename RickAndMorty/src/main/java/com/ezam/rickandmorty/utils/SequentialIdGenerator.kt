package com.ezam.rickandmorty.utils

import com.ezam.rickandmorty.domain.IdGenerator

class SequentialIdGenerator(private val range: IntRange) : IdGenerator {
    private var current = range.last

    override fun nextId(): Int {
        current = (current + range.step - range.first) % ( range.last - range.start + 1) + range.first
        return current
    }
}