package com.example.punky

import android.app.Application
import com.example.punky.app.data.GoogleMobileServices
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