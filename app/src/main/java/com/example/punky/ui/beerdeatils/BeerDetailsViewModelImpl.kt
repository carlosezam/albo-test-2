package com.example.punky.ui.beerdeatils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.punky.data.local.daos.BeerDao
import kotlinx.coroutines.launch
import javax.inject.Inject

class BeerDetailsViewModelImpl @Inject constructor(
    private val beerDao: BeerDao
): BeerDetailsViewModel() {

    override val beerImage = MutableLiveData<String?>()

    override fun loadBeer(beerId: Int) {
        viewModelScope.launch {
            var beer = beerDao.getById( beerId ) ?: throw IllegalStateException("")

            beerImage.value = beer.imageUrl
        }
    }

}