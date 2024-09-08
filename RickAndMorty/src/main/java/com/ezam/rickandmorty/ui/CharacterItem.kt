package com.ezam.rickandmorty.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import com.ezam.rickandmorty.R
import com.ezam.rickandmorty.domain.Character

@Composable
fun CharacterItem(character: Character, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }
    var dominantColor by remember { mutableStateOf(Color.Gray) }
    var textColor by remember { mutableStateOf(Color.White) }
    var isLoading by remember { mutableStateOf(true) }
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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                style = MaterialTheme.typography.headlineMedium,
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
        } else {

            bitmapImage?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen de ${character.name}",
                    modifier = Modifier
                        .size(maxWidth.div(2))
                        .border(4.dp, dominantColor, CircleShape)
                        .padding(1.dp)
                        .clip(CircleShape)
                        ,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    CharacterItem(
        character = Character(
            "Rick",
            "https://rickandmortyapi.com/api/character/avatar/3.jpeg"
        )
    )
}