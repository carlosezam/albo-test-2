package com.example.punky.utils

import android.content.Context
import android.graphics.PorterDuff
import androidx.annotation.ColorRes
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

class LoadingDrawable(context: Context, @ColorRes colorId: Int) : CircularProgressDrawable(context) {
    init {
        strokeWidth = 5f
        centerRadius = 12f
        colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.resources.getColor(colorId),
            BlendModeCompat.SRC_IN
        )
        start()
    }
}