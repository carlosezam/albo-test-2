package com.example.punky.ui.beerdeatils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

open abstract class BeerDetailsViewModel : ViewModel() {
    abstract val beerImage: LiveData<String?>

    abstract fun loadBeer(beerId: Int)
}