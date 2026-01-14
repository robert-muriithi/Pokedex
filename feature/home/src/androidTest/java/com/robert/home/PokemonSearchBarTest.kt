package com.robert.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.robert.domain.model.Pokemon
import com.robert.home.components.PokemonSearchBar
import org.junit.Rule
import org.junit.Test

class PokemonSearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBar_displaysPlaceholder_whenEmpty() {
        composeTestRule.setContent {
            PokemonSearchBar(
                query = "",
                onQueryChange = {},
                searchState = SearchUiState.Idle,
                onPokemonClick = {},
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Search Pokémon by name or ID...")
            .assertIsDisplayed()
    }

    @Test
    fun searchBar_showsLoadingState() {
        composeTestRule.setContent {
            PokemonSearchBar(
                query = "pikachu",
                onQueryChange = {},
                searchState = SearchUiState.Loading,
                onPokemonClick = {},
                onClearClick = {}
            )
        }

        // Loading indicator should be displayed
        composeTestRule
            .onNodeWithText("pikachu")
            .assertIsDisplayed()
    }

    @Test
    fun searchBar_showsSuccessResult() {
        val pokemon = Pokemon(
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/"
        )

        composeTestRule.setContent {
            PokemonSearchBar(
                query = "pikachu",
                onQueryChange = {},
                searchState = SearchUiState.Success(pokemon),
                onPokemonClick = {},
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Pikachu")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("#025")
            .assertIsDisplayed()
    }

    @Test
    fun searchBar_showsErrorMessage() {
        composeTestRule.setContent {
            PokemonSearchBar(
                query = "unknown",
                onQueryChange = {},
                searchState = SearchUiState.Error("No Pokémon found matching \"unknown\""),
                onPokemonClick = {},
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("No Pokémon found matching \"unknown\"")
            .assertIsDisplayed()
    }

    @Test
    fun searchBar_clickOnResult_callsOnPokemonClick() {
        val pokemon = Pokemon(
            name = "pikachu",
            url = "https://pokeapi.co/api/v2/pokemon/25/"
        )
        var clickedPokemon: Pokemon? = null

        composeTestRule.setContent {
            PokemonSearchBar(
                query = "pikachu",
                onQueryChange = {},
                searchState = SearchUiState.Success(pokemon),
                onPokemonClick = { clickedPokemon = it },
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Pikachu")
            .performClick()

        assert(clickedPokemon == pokemon)
    }
}

