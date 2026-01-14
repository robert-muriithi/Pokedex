package com.robert.domain.repository

import androidx.paging.PagingData
import com.robert.domain.model.Pokemon
import com.robert.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<Pokemon>>
    suspend fun getPokemonDetail(name: String): Result<PokemonDetail>
    suspend fun searchPokemon(query: String): Result<Pokemon>
}

