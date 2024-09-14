package com.example.punky.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun imageRequest(data: Any?, size: Size = Size.ORIGINAL): ImageRequest {
    val context = LocalContext.current
    return ImageRequest.Builder(context).data(data).size(size).build()
}