package com.robert.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.robert.data.mapper.toDomain
import com.robert.data.paging.PokemonRemoteMediator
import com.robert.database.PokemonDatabase
import com.robert.domain.model.Pokemon
import com.robert.domain.model.PokemonDetails
import com.robert.domain.repository.PokemonRepository
import com.robert.network.api.PokemonApiService
import com.robert.network.util.NetworkConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase
) : PokemonRepository {

    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory = { database.pokemonDao().getAllPokemon() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE * 2
            ),
            remoteMediator = PokemonRemoteMediator(
                apiService = apiService,
                database = database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetails> {
        return try {
            val response = apiService.getPokemonDetail(name)
            Timber.d("Successfully fetched Pokemon detail: ${response.name}")
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Timber.e(e, "Error fetching Pokemon detail for: $name")
            Result.failure(e)
        }
    }

    override suspend fun searchPokemon(query: String): Result<Pokemon> {
        return try {
            val localResult = database.pokemonDao().searchPokemon(query)
            if (localResult != null) {
                Timber.d("Found Pokemon in local database: ${localResult.name}")
                return Result.success(localResult.toDomain())
            }

            val response = apiService.getPokemonDetail(query)
            Timber.d("Found Pokemon from API: ${response.name}")
            Result.success(
                Pokemon(
                    id = response.id,
                    name = response.name,
                    url = "${NetworkConstants.POKEMON_API_BASE_URL}pokemon/${response.id}/",
                    imageUrl = "${NetworkConstants.POKEMON_IMAGE_BASE_URL}${response.id}.png"
                )
            )
        } catch (e: Exception) {
            Timber.e(e, "Error searching Pokemon: $query")
            Result.failure(e)
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
    }
}

