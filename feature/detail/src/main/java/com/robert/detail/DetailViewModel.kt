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
class DetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    
    fun loadPokemonDetail(name: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            getPokemonDetailUseCase(name)
                .onSuccess { detail ->
                    _uiState.value = DetailUiState.Success(detail)
                }
                .onFailure { error ->
                    _uiState.value = DetailUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
