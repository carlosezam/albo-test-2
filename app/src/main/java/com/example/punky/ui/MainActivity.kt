package com.example.punky.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.ui.character.CharacterItem
import com.ezam.rickandmorty.ui.character.CharacterItemViewModel
import com.ezam.rickandmorty.ui.character.CharacterScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var repository: CharacterRepository

    private val vmodel: CharacterItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val state by vmodel.getState().collectAsState()

            CharacterScreen(state = state){
                vmodel.nexRandom()
            }

            LaunchedEffect(key1 = Unit) {
                vmodel.nexRandom()
            }
        }
    }
}