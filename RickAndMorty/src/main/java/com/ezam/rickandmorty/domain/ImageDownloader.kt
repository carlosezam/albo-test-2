package com.ezam.rickandmorty.domain

interface ImageDownloader {
    suspend fun downloadImageAsByteArray(imageUrl: String) : ByteArray?
}