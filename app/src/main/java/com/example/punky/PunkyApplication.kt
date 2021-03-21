package com.example.punky

import android.app.Application
import com.example.punky.di.ApplicationGraph
import com.example.punky.di.DaggerApplicationGraph

class PunkyApplication : Application() {

    val appComponent = DaggerApplicationGraph.factory().create(this)
}