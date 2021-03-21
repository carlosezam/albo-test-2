package com.example.punky.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.punky.data.network.PunkApi
import com.example.punky.data.network.PunkApiSource
import com.example.punky.data.network.PunkBeer
import kotlinx.coroutines.flow.Flow

class PunkApiRepository(
    private val service: PunkApi
) {
    fun getStream() : Flow<PagingData<PunkBeer>> {
        val pagingConfig = PagingConfig(
            pageSize = PUNK_API_ITEMS_PER_PAGE,
            enablePlaceholders = false
        )
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PunkApiSource(service) }
        ).flow
    }
}