package com.robert.domain.usecase

import androidx.paging.PagingData
import com.robert.domain.repository.PokemonRepository
import com.robert.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<PagingData<Pokemon>> {
        return repository.getPokemonList()
    }
}
