package com.example.punky

import android.app.Application
import android.util.Log
import com.example.punky.app.data.GoogleMobileServices
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PunkyApplication : Application() {

    @Inject
    lateinit var mobileServices: GoogleMobileServices

    override fun onCreate() {
        super.onCreate()

        mobileServices.initialize(this)
        mobileServices.initializeRemoteConfig()
    }
}