package com.robert.domain.repository

import androidx.paging.PagingData
import com.robert.domain.model.Pokemon
import com.robert.domain.model.PokemonDetails
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<Pokemon>>
    suspend fun getPokemonDetail(name: String): Result<PokemonDetails>
    suspend fun searchPokemon(query: String): Result<Pokemon>
}

