package com.robert.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.robert.design_system.components.PokedexLoadingIndicator
import com.robert.domain.model.Pokemon
import com.robert.home.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonGridContent(
    pokemonList: LazyPagingItems<Pokemon>,
    onPokemonClick: (Pokemon) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (pokemonList.loadState.refresh) {
            is LoadState.Loading -> {
                PokedexLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is LoadState.Error -> {
                ErrorMessage(
                    message = stringResource(R.string.failed_to_load_pok_mon),
                    onRetry = { pokemonList.retry() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        count = pokemonList.itemCount,
                        key = pokemonList.itemKey { it.name }
                    ) { index ->
                        pokemonList[index]?.let { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                onClick = { onPokemonClick(pokemon) },
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }

                    if (pokemonList.loadState.append is LoadState.Loading) {
                        item(key = "loading_indicator") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                PokedexLoadingIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

