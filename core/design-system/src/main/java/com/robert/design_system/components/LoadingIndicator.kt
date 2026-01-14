package com.robert.design_system.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.robert.design_system.R
import com.robert.design_system.theme.PokemonTheme

@Composable
fun PokedexLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    @RawRes animationRes: Int = R.raw.pokeball_loading,
    message: String? = null
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animationRes)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(size)
            )

            if (message != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PokedexFullScreenLoading(
    modifier: Modifier = Modifier,
    message: String = "Catching Pokémon..."
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PokedexLoadingIndicator(
            size = 150.dp,
            message = message
        )
    }
}

@Preview(name = "PokemonLoadingIndicator - Default", showBackground = true)
@Composable
private fun PokedexLoadingIndicatorDefaultPreview() {
    PokemonTheme {
        Surface {
            PokedexLoadingIndicator()
        }
    }
}

@Preview(name = "PokemonLoadingIndicator - With Message", showBackground = true)
@Composable
private fun PokedexLoadingIndicatorWithMessagePreview() {
    PokemonTheme {
        Surface {
            PokedexLoadingIndicator(
                message = "Loading Pokémon data..."
            )
        }
    }
}

@Preview(name = "PokemonLoadingIndicator - Small", showBackground = true)
@Composable
private fun PokedexLoadingIndicatorSmallPreview() {
    PokemonTheme {
        Surface {
            PokedexLoadingIndicator(
                size = 80.dp,
                message = "Searching..."
            )
        }
    }
}

@Preview(name = "PokemonFullScreenLoading", showBackground = true)
@Composable
private fun PokedexFullScreenLoadingPreview() {
    PokemonTheme {
        Surface {
            PokedexFullScreenLoading(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

