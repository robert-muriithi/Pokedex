package com.robert.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robert.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()
    
    fun loadPokemonDetail(name: String) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            getPokemonDetailUseCase(name)
                .onSuccess { detail ->
                    _uiState.value = DetailsUiState.Success(detail)
                }
                .onFailure { error ->
                    _uiState.value = DetailsUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
