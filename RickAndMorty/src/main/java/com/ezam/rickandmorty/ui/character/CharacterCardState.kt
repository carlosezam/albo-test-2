package com.ezam.rickandmorty.ui.character

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.ezam.rickandmorty.domain.VitalStatus

data class CharacterCardState(
    val isLoading: Boolean,
    val name: String,
    val status: VitalStatus,
    val image: ImageBitmap,
    val primaryColor: Color,
    val textColor: Color,
)