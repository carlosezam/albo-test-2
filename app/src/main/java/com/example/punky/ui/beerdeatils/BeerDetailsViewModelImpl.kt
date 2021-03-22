package com.example.punky.ui.beerdeatils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.punky.data.local.daos.BeerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BeerDetailsViewModelImpl @Inject constructor(
    private val beerDao: BeerDao
): BeerDetailsViewModel() {

    override val beerDetails = MutableLiveData<UiBeerDetails?>()

    override fun loadBeer(beerId: Int) {
        viewModelScope.launch( Dispatchers.IO ) {
            var beer = beerDao.getById( beerId ) ?: throw IllegalStateException("")

            var ui = UiBeerDetails( beer.name, beer.imageUrl, beer.tagline, beer.description, beer.first_brewed, beer.food_pairing )

            beerDetails.postValue( ui )
        }
    }

}