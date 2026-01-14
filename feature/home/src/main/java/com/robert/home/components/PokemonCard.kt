package com.robert.home.components

import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.robert.design_system.theme.PokemonTheme
import com.robert.domain.model.Pokemon
import com.robert.home.util.extractDominantColor
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    var dominantColor by remember(pokemon.id) { mutableStateOf(Color(0xFFE0E0E0)) }
    val context = LocalContext.current

    val imageRequest = remember(pokemon.imageUrl) {
        ImageRequest.Builder(context)
            .data(pokemon.imageUrl)
            .crossfade(true)
            .allowHardware(false)
            .build()
    }

    val formattedName = remember(pokemon.name) {
        pokemon.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    }

    with(sharedTransitionScope) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                dominantColor.copy(alpha = 0.3f),
                                dominantColor.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubcomposeAsyncImage(
                        model = imageRequest,
                        contentDescription = pokemon.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "pokemon_image_${pokemon.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.Fit,
                        onSuccess = { result ->
                            val bitmap = (result.result.drawable as? BitmapDrawable)?.bitmap
                            if (bitmap != null) {
//                                val color =
                                dominantColor = extractDominantColor(bitmap = bitmap)
                            }
                        },
                        success = {
                            SubcomposeAsyncImageContent()
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formattedName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "pokemon_name_${pokemon.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "PokemonCard - Pikachu", showBackground = true)
@Composable
private fun PokemonCardPikachuPreview() {
    PokemonTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                PokemonCard(
                    pokemon = Pokemon(
                        name = "pikachu",
                        url = "https://pokeapi.co/api/v2/pokemon/25/"
                    ),
                    onClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "PokemonCard - Bulbasaur", showBackground = true)
@Composable
private fun PokemonCardBulbasaurPreview() {
    PokemonTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                PokemonCard(
                    pokemon = Pokemon(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/1/"
                    ),
                    onClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "PokemonCard - Grid", showBackground = true, widthDp = 400)
@Composable
private fun PokemonCardGridPreview() {
    PokemonTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PokemonCard(
                        pokemon = Pokemon("charmander", "https://pokeapi.co/api/v2/pokemon/4/"),
                        onClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        modifier = Modifier.weight(1f)
                    )
                    PokemonCard(
                        pokemon = Pokemon("squirtle", "https://pokeapi.co/api/v2/pokemon/7/"),
                        onClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

