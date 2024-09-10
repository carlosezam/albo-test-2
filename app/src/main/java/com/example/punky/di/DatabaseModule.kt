package com.example.punky.di

import android.content.Context
import androidx.room.Room
import com.ezam.rickandmorty.data.local.RickandmortyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesRickandmortyBD(@ApplicationContext context: Context): RickandmortyDatabase {
        return Room.databaseBuilder(
            context,
            RickandmortyDatabase::class.java,
            RickandmortyDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun providesCharacterDao(database: RickandmortyDatabase) = database.characterDao()
}