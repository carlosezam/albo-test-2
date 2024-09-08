package com.example.punky.di

import android.app.Application
import com.example.punky.data.local.PunkyDatabase
import com.example.punky.data.local.daos.BeerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    fun providesRoomDatabase(app: Application): PunkyDatabase {
        return PunkyDatabase.getInstance( app )
    }

    @Provides
    fun providesBeerDao( punkyDatabase: PunkyDatabase): BeerDao {
        return punkyDatabase.beerDao()
    }
}