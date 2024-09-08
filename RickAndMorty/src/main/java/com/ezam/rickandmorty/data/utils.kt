package com.ezam.rickandmorty.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    if(byteArray.isEmpty())
        return null
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}