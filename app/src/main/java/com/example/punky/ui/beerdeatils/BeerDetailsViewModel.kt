package com.example.punky.ui.beerdeatils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BeerDetailsViewModel : ViewModel() {

    abstract val beerDetails: LiveData<UiBeerDetails?>

    abstract fun loadBeer(beerId: Int)
}