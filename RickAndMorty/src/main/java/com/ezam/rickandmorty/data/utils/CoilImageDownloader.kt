package com.ezam.rickandmorty.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.imageLoader
import coil.request.ImageRequest
import com.ezam.rickandmorty.domain.ImageDownloader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class CoilImageDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageDownloader {

    private val format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    private val quality: Int = 100

    override suspend fun downloadImageAsByteArray(imageUrl: String): ByteArray? = withContext(Dispatchers.IO){
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = context.imageLoader.execute(request)

        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap

        bitmap?.let {
            val outputStream = ByteArrayOutputStream()
            it.compress(format, quality, outputStream)
            outputStream.toByteArray()
        }
    }
}