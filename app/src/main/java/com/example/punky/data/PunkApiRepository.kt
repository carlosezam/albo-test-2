package com.example.punky.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.punky.data.local.PunkyDatabase
import com.example.punky.data.local.entities.Beer
import com.example.punky.data.network.PunkApi
import com.example.punky.data.network.PunkApiSource
import com.example.punky.data.network.PunkBeer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PunkApiRepository @Inject constructor(
    private val service: PunkApi,
    private val database: PunkyDatabase
) {
    fun _getStream() : Flow<PagingData<PunkBeer>> {
        val pagingConfig = PagingConfig(
            pageSize = PUNK_API_ITEMS_PER_PAGE,
            enablePlaceholders = false
        )
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PunkApiSource(service) }
        ).flow
    }

    @ExperimentalPagingApi
    fun getStream() : Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = PUNK_API_ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        remoteMediator = PunkRemoteMediator(
            database,
            service
        ),
        pagingSourceFactory = { database.beerDao().pagingSource() }
    ).flow
}