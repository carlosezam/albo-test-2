package com.example.punky.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.punky.data.local.entities.RemoteBeerKeys

@Dao
interface RemoteBeerKeysDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( remoteKeys: List<RemoteBeerKeys>)

    @Query("SELECT * FROM remote_beer_keys WHERE beerId = :id")
    suspend fun remoreKeyByRepoId( id: Int) : RemoteBeerKeys

    @Query("DELETE FROM remote_beer_keys")
    suspend fun deleteAll()

    @Query("SELECT * FROM REMOTE_BEER_KEYS ORDER BY beerId DESC LIMIT 1")
    suspend fun getLast(): RemoteBeerKeys
}