package com.robert.domain.usecase

import com.robert.domain.repository.PokemonRepository
import com.robert.domain.model.PokemonDetails
import javax.inject.Inject


class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Result<PokemonDetails> {
        return repository.getPokemonDetail(name)
    }
}

