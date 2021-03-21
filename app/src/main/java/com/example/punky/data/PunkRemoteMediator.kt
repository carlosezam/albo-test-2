package com.example.punky.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.punky.data.local.PunkyDatabase
import com.example.punky.data.local.entities.Beer
import com.example.punky.data.local.entities.RemoteBeerKeys
import com.example.punky.data.network.PunkApi
import java.io.InvalidObjectException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PunkRemoteMediator @Inject constructor(
    private val database: PunkyDatabase,
    private val service: PunkApi
): RemoteMediator<Int, Beer>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Beer>): MediatorResult {
        return try {
            val page = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: PUNK_API_STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysForFirstItem(state)
                        ?: throw InvalidObjectException("Remote keys should not be null")
                    remoteKeys.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    remoteKeys?.nextKey ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                }
            }

            val beers = service.getBeers(page, state.config.pageSize).map {
                Beer(it.id, it.name, it.image_url, it.tagline, it.description, it.first_brewed, it.food_pairing)
            }
            val endOfPaginationReached = beers.isEmpty()
            database.withTransaction {

                if(loadType == LoadType.REFRESH){
                    database.beerDao().deleteAll()
                    database.remoteBeerKeysDao().deleteAll()
                }

                val prevKey = if(page == PUNK_API_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val remoteKeys = beers.map {
                    RemoteBeerKeys( it.id, prevKey, nextKey)
                }
                database.remoteBeerKeysDao().insertAll(remoteKeys)
                database.beerDao().insertAll(beers)
            }
            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        }catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, Beer>): RemoteBeerKeys?{
        val lastPageWithItems = state.pages.lastOrNull(){ it.data.isNotEmpty() }
        val lastItem = lastPageWithItems?.data?.lastOrNull() ?: return null
        return database.remoteBeerKeysDao().remoreKeyByRepoId(lastItem.id)
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, Beer>): RemoteBeerKeys?{
        val firstPageWithItems = state.pages.firstOrNull(){ it.data.isNotEmpty() }
        val firstItem = firstPageWithItems?.data?.firstOrNull() ?: return null
        return database.remoteBeerKeysDao().remoreKeyByRepoId(firstItem.id)
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, Beer>): RemoteBeerKeys? {
        val closestItem = state.anchorPosition?.let { state.closestItemToPosition( it ) } ?: return null
        return database.remoteBeerKeysDao().remoreKeyByRepoId( closestItem.id )
    }
}