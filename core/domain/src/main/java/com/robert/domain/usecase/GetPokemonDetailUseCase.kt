package com.robert.domain.usecase

import com.robert.domain.repository.PokemonRepository
import com.robert.domain.model.PokemonDetail
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Result<PokemonDetail> {
        return repository.getPokemonDetail(name)
    }
}

