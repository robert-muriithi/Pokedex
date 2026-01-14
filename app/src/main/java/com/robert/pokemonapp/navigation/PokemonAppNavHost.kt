package com.robert.pokemonapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.robert.detail.DetailScreen
import com.robert.home.HomeScreen
import com.robert.navigation.Screen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonNavHost() {
    var currentScreen: Screen by remember { mutableStateOf(Screen.Home) }
    val gridState = rememberLazyGridState()

    BackHandler(enabled = currentScreen is Screen.Details) {
        currentScreen = Screen.Home
    }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                is Screen.Home -> {
                    HomeScreen(
                        onPokemonClick = { pokemon ->
                            currentScreen = Screen.Details(pokemon)
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        gridState = gridState
                    )
                }
                is Screen.Details -> {
                    DetailScreen(
                        pokemon = screen.pokemon,
                        onBackClick = { currentScreen = Screen.Home },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}