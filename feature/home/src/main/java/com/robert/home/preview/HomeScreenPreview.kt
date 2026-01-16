package com.robert.home.preview

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robert.design_system.theme.PokemonTheme
import com.robert.domain.model.Pokemon
import com.robert.home.R
import com.robert.home.SearchUiState
import com.robert.home.UiText
import com.robert.home.components.PokemonSearchBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Preview(name = "HomeScreen - Content", showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    PokemonTheme {
        var searchQuery by remember { mutableStateOf("") }
        var searchState by remember { mutableStateOf<SearchUiState>(SearchUiState.Idle) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Pokédex",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PokemonSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    searchState = searchState,
                    onPokemonClick = { },
                    onClearClick = {
                        searchQuery = ""
                        searchState = SearchUiState.Idle
                    }
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Pokemon Grid Content",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "HomeScreen - With Search Query", showBackground = true)
@Composable
fun HomeScreenWithSearchPreview() {
    PokemonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Pokédex",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PokemonSearchBar(
                    query = "pikachu",
                    onQueryChange = { },
                    searchState = SearchUiState.Success(
                        Pokemon(
                            id = 25,
                            name = "pikachu",
                            url = "",
                            imageUrl = ""
                        )
                    ),
                    onPokemonClick = { },
                    onClearClick = { }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "HomeScreen - Search Error", showBackground = true)
@Composable
fun HomeScreenSearchErrorPreview() {
    PokemonTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Pokédex",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PokemonSearchBar(
                    query = "unknownpokemon",
                    onQueryChange = { },
                    searchState = SearchUiState.Error(
                        UiText.StringResource(R.string.pok_mon_not_found)
                    ),
                    onPokemonClick = { },
                    onClearClick = { }
                )
            }
        }
    }
}

