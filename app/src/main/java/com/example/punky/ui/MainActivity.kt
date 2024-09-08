package com.example.punky.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope

import com.example.punky.R
import com.ezam.rickandmorty.RandomIdGenerator
import com.ezam.rickandmorty.data.remote.RickandmortyApiRest
import com.ezam.rickandmorty.data.CharactersRepositoryImpl
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.CharacterRepository
import com.ezam.rickandmorty.ui.CharacterItem
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var repository: CharacterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scope = rememberCoroutineScope()
            var character by remember { mutableStateOf<Character?>(null) }

            LaunchedEffect(key1 = Unit) {
                character = repository.randomCharacter()
            }

            character?.let {
                CharacterItem(character = it){
                    scope.launch {
                        character = repository.randomCharacter()
                    }
                }
            }
        }
    }
}