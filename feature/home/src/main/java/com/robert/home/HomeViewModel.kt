package com.robert.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.robert.domain.model.Pokemon
import com.robert.domain.usecase.GetPokemonListUseCase
import com.robert.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase
) : ViewModel() {

    val pokemonList: Flow<PagingData<Pokemon>> = getPokemonListUseCase()
        .cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    init {
        _searchQuery
            .debounce(DEBOUNCE_DELAY_MS)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .onEach { query -> performSearch(query) }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            clearSearch()
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchState.value = SearchUiState.Idle
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading
            searchPokemonUseCase(query)
                .onSuccess { pokemon ->
                    _searchState.value = SearchUiState.Success(pokemon)
                }
                .onFailure { error ->
                    val message = when {
                        error.message?.contains("404") == true -> UiText.StringResource(
                            R.string.no_pok_mon_found_matching,
                            listOf(query)
                        )

                        error.message?.contains("Unable to resolve host") == true -> UiText.StringResource(
                            R.string.error_no_internet
                        )

                        error.message?.contains("timeout") == true -> UiText.StringResource(R.string.error_timeout)
                        else -> UiText.StringResource(R.string.pok_mon_not_found, listOf(query))
                    }
                    _searchState.value = SearchUiState.Error(message)
                }
        }
    }

    companion object {
        const val DEBOUNCE_DELAY_MS = 500L
    }
}

