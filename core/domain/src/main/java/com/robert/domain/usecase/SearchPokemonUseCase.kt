package com.robert.domain.usecase

import com.robert.domain.repository.PokemonRepository
import com.robert.domain.model.Pokemon
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(query: String): Result<Pokemon> {
        return repository.searchPokemon(query.lowercase().trim())
    }
}

