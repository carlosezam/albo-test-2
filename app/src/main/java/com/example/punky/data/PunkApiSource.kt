package com.example.punky.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.punky.data.network.PunkApi
import com.example.punky.data.network.PunkBeer
import com.example.punky.ui.beerlist.BeerItem
import com.example.punky.ui.beerlist.BeerListViewModel
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val PUNK_API_STARTING_PAGE_INDEX = 1
const val PUNK_API_ITEMS_PER_PAGE = 10





