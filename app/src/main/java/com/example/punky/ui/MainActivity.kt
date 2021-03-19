package com.example.punky.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.punky.R
import com.example.punky.data.network.PunkApi
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenResumed {
            val client = HttpClient(CIO) {
                install(JsonFeature){
                    val json = kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                    serializer = KotlinxSerializer( json )
                }
            }
            val api = PunkApi( client )

            api.getBeers(1, 20)
        }
    }
}