package com.robert.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.robert.domain.model.Pokemon
import com.robert.home.components.PokemonCard
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalSharedTransitionApi::class)
class PokemonCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testPokemon = Pokemon(
        name = "bulbasaur",
        url = "https://pokeapi.co/api/v2/pokemon/1/"
    )

    @Test
    fun pokemonCard_displaysPokemonName() {
        composeTestRule.setContent {
            SharedTransitionLayout {
                androidx.compose.animation.AnimatedVisibility(visible = true) {
                    PokemonCard(
                        pokemon = testPokemon,
                        onClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("Bulbasaur")
            .assertIsDisplayed()
    }

    @Test
    fun pokemonCard_hasCorrectContentDescription() {
        composeTestRule.setContent {
            SharedTransitionLayout {
                androidx.compose.animation.AnimatedVisibility(visible = true) {
                    PokemonCard(
                        pokemon = testPokemon,
                        onClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithContentDescription("bulbasaur")
            .assertIsDisplayed()
    }

    @Test
    fun pokemonCard_onClick_triggersCallback() {
        var clicked = false

        composeTestRule.setContent {
            SharedTransitionLayout {
                androidx.compose.animation.AnimatedVisibility(visible = true) {
                    PokemonCard(
                        pokemon = testPokemon,
                        onClick = { clicked = true },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("Bulbasaur")
            .performClick()

        assert(clicked)
    }

    @Test
    fun pokemonCard_displaysDifferentPokemon() {
        val pikachu = Pokemon(
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/"
        )

        composeTestRule.setContent {
            SharedTransitionLayout {
                androidx.compose.animation.AnimatedVisibility(visible = true) {
                    PokemonCard(
                        pokemon = pikachu,
                        onClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("Pikachu")
            .assertIsDisplayed()
    }
}

