package com.example.punky.ui.beerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.punky.data.PunkApiRepository
import com.example.punky.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BeerListViewModelImpl @Inject constructor(
    private val repository: PunkApiRepository
): BeerListViewModel() {

    override val command = MutableLiveData<Event<BeerListCommand>>()

    @ExperimentalPagingApi
    override val beers : Flow<PagingData<BeerItem>> = repository.getStream().map { pagingData ->
        pagingData.map { BeerItem(it.id, it.name, it.description, it.imageUrl) }
    }.cachedIn( viewModelScope )

    override fun clickOnBeerItem(item: BeerItem) {
        command.value = Event( BeerListCommand.OpenBeerDetails( item.id ) )
    }
}