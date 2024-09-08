package com.ezam.rickandmorty.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import com.ezam.rickandmorty.R
import com.ezam.rickandmorty.domain.Character
import com.ezam.rickandmorty.domain.VitalStatus

@Composable
fun CharacterItem(character: Character, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }
    var dominantColor by remember { mutableStateOf(Color.Gray) }
    var textColor by remember { mutableStateOf(Color.White) }
    var isLoading by remember { mutableStateOf(true) }
    val statusColor = when(character.status) {
        VitalStatus.Alive -> Color.Green
        VitalStatus.Dead -> Color.Red
        VitalStatus.Unknown -> Color.Gray
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .padding(top = maxWidth.div(4))
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = dominantColor
            )
        ) {
            Spacer(modifier = Modifier.height(this@BoxWithConstraints.maxWidth.div(4)))
            Text(
                text = character.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = textColor,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = character.status.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = textColor,
            )
        }

        val context = LocalContext.current

        LaunchedEffect(key1 = character.imageUrl) {

            isLoading = true

            val request = ImageRequest.Builder(context)
                .data(character.imageUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .allowHardware(false)
                .build()

            val result = context.imageLoader.execute(request).drawable as? BitmapDrawable

            result?.bitmap?.let { bitmap ->
                bitmapImage = bitmap
                Palette.from(bitmap).generate().dominantSwatch?.let {
                    dominantColor = Color(it.rgb)
                    textColor = Color(it.titleTextColor)
                }
            }

            isLoading = false
        }

        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }


        Image(
            bitmap = bitmapImage?.asImageBitmap() ?: ImageBitmap.imageResource(id = R.drawable.rick),
            contentDescription = "Imagen de ${character.name}",
            modifier = Modifier
                .size(maxWidth.div(2))
                //.border(4.dp, dominantColor, CircleShape)
                .padding(1.dp)
                //.clip(CircleShape)
                .graphicsLayer {
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
                                color = dominantColor,
                                radius = size.width.div(2) + 2,
                                alpha = 0.5f,
                                style = Stroke(width = 10.dp.toPx())
                            )
                        }


                        val dotSize = size.width / 8f
                        drawCircle(
                            Color.Black,
                            radius = dotSize,
                            center = Offset(size.width - dotSize, size.height - dotSize),
                            blendMode = BlendMode.Clear
                        )

                        drawCircle(
                            color = statusColor,
                            radius = dotSize * 0.8f,
                            center = Offset(size.width - dotSize, size.height - dotSize),
                        )
                    }
                }
                ,
            contentScale = ContentScale.FillWidth
        )


    }
}

@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    CharacterItem(
        character = Character(
            "Rick",
            "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
            status = VitalStatus.Alive
        )
    )
}