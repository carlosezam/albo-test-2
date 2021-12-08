package com.example.punky.data.local.daos

import androidx.paging.PagingSource
import androidx.room.*
import com.example.punky.data.local.entities.Beer

@Dao
abstract class BeerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(beers: List<Beer>)

    @Query("SELECT * FROM beers")
    abstract fun pagingSource(): PagingSource<Int, Beer>

    @Query("DELETE FROM beers")
    abstract suspend fun deleteAll(): Int

    @Query("SELECT * FROM beers")
    abstract suspend fun getAll() : List<Beer>

    @Query("SELECT * FROM beers WHERE id = :id")
    abstract suspend fun getById(id: Int) : Beer?

    @Insert( onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertOtIgnore(beer: Beer) : Long

    @Update
    abstract suspend fun update(beer: Beer)

    @Delete
    abstract suspend fun delete(beer: Beer): Int

    @Transaction
    open suspend fun upsert(beer: Beer){
        if( insertOtIgnore( beer) == -1L ){
            update(beer)
        }
    }
}