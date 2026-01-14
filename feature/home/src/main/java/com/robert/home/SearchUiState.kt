package com.robert.home

import androidx.compose.runtime.Immutable
import com.robert.domain.model.Pokemon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface SearchUiState {
    @Immutable
    data object Idle : SearchUiState

    @Immutable
    data object Loading : SearchUiState

    @Immutable
    data class Success(val pokemon: Pokemon) : SearchUiState

    @Immutable
    data class Error(val message: String) : SearchUiState
}

