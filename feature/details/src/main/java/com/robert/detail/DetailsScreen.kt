package com.robert.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robert.design_system.components.PokedexLoadingIndicator
import com.robert.detail.components.DetailsScreenAppBar
import com.robert.detail.components.PokemonDetailContent
import com.robert.domain.model.Pokemon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DetailsScreen(
    pokemon: Pokemon,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pokemon.name) {
        viewModel.loadPokemonDetail(pokemon.name)
    }

    Scaffold(
        topBar = {
            DetailsScreenAppBar(sharedTransitionScope, pokemon, animatedVisibilityScope, onBackClick)
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is DetailsUiState.Loading -> {
                LoadingComponent(paddingValues)
            }
            is DetailsUiState.Error -> {
                ErrorContent(paddingValues, state, viewModel, pokemon)
            }
            is DetailsUiState.Success -> {
                PokemonDetailContent(
                    pokemonDetails = state.pokemonDetails,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    paddingValues: PaddingValues,
    state: DetailsUiState.Error,
    viewModel: DetailsViewModel,
    pokemon: Pokemon
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.error, state.message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.loadPokemonDetail(pokemon.name) }) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@Composable
private fun LoadingComponent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        PokedexLoadingIndicator(
            message = stringResource(R.string.loading_pok_mon_data)
        )
    }
}
