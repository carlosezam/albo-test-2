package com.example.punky.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.punky.data.PUNK_API_ITEMS_PER_PAGE
import com.example.punky.data.PUNK_API_STARTING_PAGE_INDEX
import io.ktor.utils.io.errors.*

class PunkApiSource(
    private val service: PunkApi
) : PagingSource<Int, PunkBeer>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PunkBeer> {
        val page = params.key ?: PUNK_API_STARTING_PAGE_INDEX
        return try {
            val beers = service.getBeers( page, params.loadSize)

            val nextKey = if( beers.isEmpty() ) null else page + ( params.loadSize / PUNK_API_ITEMS_PER_PAGE )
            val prevKey = if( page == PUNK_API_STARTING_PAGE_INDEX) null else page -1

            LoadResult.Page( beers, prevKey, nextKey )

        }catch ( exception: IOException){
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PunkBeer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}