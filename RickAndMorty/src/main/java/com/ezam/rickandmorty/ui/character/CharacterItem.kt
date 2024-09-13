package com.ezam.rickandmorty.ui.character

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ezam.rickandmorty.R
import com.ezam.rickandmorty.domain.VitalStatus

data class CharacterItemState(
    val isLoading: Boolean,
    val name: String,
    val status: VitalStatus,
    val image: ImageBitmap,
    val primaryColor: Color,
    val textColor: Color,
)

class DebounceClick( val onClick: () -> Unit ) : () -> Unit {
    override fun invoke(): Unit = onClick()

}

@Composable
fun CharacterScreen(state: CharacterItemState?, modifier: Modifier = Modifier,  onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if(state != null)
            CharacterItem(character = state, onClick = DebounceClick(onClick))
        else
            CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun CharacterItem(
    character: CharacterItemState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    val statusColor = when (character.status) {
        VitalStatus.Alive -> Color.Green
        VitalStatus.Dead -> Color.Red
        VitalStatus.Unknown -> Color.Gray
    }
    val animatedPrimaryColor = animateColorAsState(targetValue = character.primaryColor)
    BoxWithConstraints(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        val radiusAvatar = maxWidth.div(4)
        Card(
            onClick = onClick,
            modifier = modifier
                .padding(top = radiusAvatar) // posiciona la card a la mitad de la imagen
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = animatedPrimaryColor.value
            )
        ) {
            Spacer(modifier = Modifier.height(radiusAvatar))
            Text(
                text = character.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = character.textColor,
            )
            Text(
                text = "Status: ${character.status.name}",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = character.textColor,
            )
            Spacer(modifier = Modifier.height(radiusAvatar.div(2)))
        }

        if (character.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
            )
        }

        Image(
            bitmap = character.image,
            contentDescription = "Imagen de ${character.name}",
            modifier = Modifier
                .size(radiusAvatar * 2)
                .decorateAvatar(statusColor, character.primaryColor),
            contentScale = ContentScale.FillWidth
        )
    }

}

@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    val state = CharacterItemState(
        isLoading = false,
        name = "Rick",
        status = VitalStatus.Dead,
        image = ImageBitmap.imageResource(id = R.drawable.rick),
        primaryColor = Color.Gray,
        textColor = Color.White
    )
    CharacterScreen(state = state)
}