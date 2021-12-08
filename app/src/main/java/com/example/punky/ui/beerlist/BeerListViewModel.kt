package com.example.punky.ui.beerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.punky.utils.Event
import kotlinx.coroutines.flow.Flow

abstract class BeerListViewModel : ViewModel() {
    @ExperimentalPagingApi
    abstract val beers : Flow<PagingData<BeerItem>>
    abstract val command: LiveData<Event<BeerListCommand>>

    abstract fun clickOnBeerItem( item: BeerItem, position: Int)
}