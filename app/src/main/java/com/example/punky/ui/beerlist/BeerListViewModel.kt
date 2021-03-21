package com.example.punky.ui.beerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.punky.data.PunkApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BeerListViewModel @Inject constructor(
    private val repository: PunkApiRepository
): ViewModel() {

    val beers : Flow<PagingData<BeerItem>> = repository.getStream().map { pagingData ->
        pagingData.map { BeerItem(it.name, it.description, it.image_url) }
    }.cachedIn( viewModelScope )

}