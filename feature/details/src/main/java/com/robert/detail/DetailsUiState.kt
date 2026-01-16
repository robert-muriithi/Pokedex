package com.robert.detail

import androidx.compose.runtime.Immutable
import com.robert.domain.model.PokemonDetails

@Immutable
sealed interface DetailsUiState {
    @Immutable
    data object Loading : DetailsUiState
    
    @Immutable
    data class Success(val pokemonDetails: PokemonDetails) : DetailsUiState
    
    @Immutable
    data class Error(val message: String) : DetailsUiState
}

