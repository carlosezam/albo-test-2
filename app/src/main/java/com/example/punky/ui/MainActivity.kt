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
import com.ezam.rickandmorty.ui.CharacterItem
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    val repository = CharactersRepositoryImpl(
        api = RickandmortyApiRest(OkHttp.create()),
        idGenerator = RandomIdGenerator(1..826)
    )

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