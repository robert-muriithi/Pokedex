package com.robert.detail

import androidx.compose.runtime.Immutable
import com.robert.domain.model.PokemonDetail

@Immutable
sealed interface DetailUiState {
    @Immutable
    data object Loading : DetailUiState
    
    @Immutable
    data class Success(val pokemonDetail: PokemonDetail) : DetailUiState
    
    @Immutable
    data class Error(val message: String) : DetailUiState
}

