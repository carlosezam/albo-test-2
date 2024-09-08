package com.ezam.rickandmorty.di

import com.ezam.rickandmorty.data.utils.CoilImageDownloader
import com.ezam.rickandmorty.domain.ImageDownloader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ImageDownloaderModule {
    @Binds
    fun bindsImageDownloader(impl: CoilImageDownloader) : ImageDownloader
}