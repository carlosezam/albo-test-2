package com.ezam.rickandmorty.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.ezam.rickandmorty.R
import com.ezam.rickandmorty.domain.VitalStatus
import com.ezam.rickandmorty.ui.character.CharacterItem
import com.ezam.rickandmorty.ui.character.CharacterItemState
import org.junit.Rule
import org.junit.Test


class CharacterItemComposeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun characterItem_debeMostrar_todosLosDatosMinimos(){

        composeRule.setContent {
            val character = CharacterItemState(
                isLoading = false,
                name = "Lorem",
                status = VitalStatus.Alive,
                image = ImageBitmap.imageResource(id = R.drawable.rick),
                primaryColor = Color.Black,
                textColor = Color.White
            )
                CharacterItem(character)
        }

        composeRule.onNodeWithText("Lorem").assertExists()
        composeRule.onNodeWithContentDescription("Imagen de Lorem").assertExists()
    }
}