package com.ezam.rickandmorty.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.VitalStatus
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


class CharacterItemComposeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun characterItem_debeMostrar_todosLosDatosMinimos(){
        val character = Character(name = "Lorem", imageUrl = "http://image.png", VitalStatus.Alive)
        composeRule.setContent { CharacterItem(character) }

        composeRule.onNodeWithText("Lorem").assertExists()
        composeRule.onNodeWithContentDescription("Imagen de Lorem").assertExists()
    }
}