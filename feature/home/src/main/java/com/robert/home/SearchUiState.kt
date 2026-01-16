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
    data class Error(val message: UiText) : SearchUiState
}

sealed class UiText {
    data class StringResource(val resId: Int, val args: List<Any> = emptyList()) : UiText()
    data class DynamicString(val value: String) : UiText()
}

