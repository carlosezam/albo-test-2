package com.ezam.rickandmorty.ui.character.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

fun Modifier.decorateAvatar(dotColor: Color, borderColor: Color) =
    graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
    }
        .drawWithCache {
            val path = Path()
            path.addOval(
                Rect(
                    topLeft = Offset.Zero,
                    bottomRight = Offset(size.width, size.height)
                )
            )
            onDrawWithContent {
                clipPath(path) {
                    this@onDrawWithContent.drawContent()
                    drawCircle(
                        color = borderColor,
                        radius = size.width.div(2) + 2,
                        alpha = 0.5f,
                        style = Stroke(width = 10.dp.toPx())
                    )
                }

                val dotSize = size.width / 8f
                drawCircle(
                    color = Color.Black,
                    radius = dotSize,
                    center = Offset(size.width - dotSize, size.height - dotSize),
                    blendMode = BlendMode.Clear
                )
                drawCircle(
                    color = dotColor,
                    radius = dotSize * 0.8f,
                    center = Offset(size.width - dotSize, size.height - dotSize),
                )
            }
        }