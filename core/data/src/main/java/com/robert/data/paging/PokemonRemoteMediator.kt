package com.robert.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.robert.data.local.database.PokemonDatabase
import com.robert.data.local.entity.PokemonEntity
import com.robert.data.local.entity.RemoteKeyEntity
import com.robert.network.api.PokemonApiService
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    private val pokemonDao = database.pokemonDao()
    private val remoteKeyDao = database.remoteKeyDao()

    companion object {
        private const val STARTING_PAGE = 0
        private const val PAGE_SIZE = 20
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        Timber.d("Loading Pokemon list, loadType: $loadType")
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val offset = page * PAGE_SIZE
            Timber.d("Fetching Pokemon list, page: $page, offset: $offset")
            val response = apiService.getPokemonList(limit = PAGE_SIZE, offset = offset)
            Timber.d("Fetched ${response.results.size} Pokemon")

            val endOfPaginationReached = response.results.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.clearAll()
                    remoteKeyDao.clearAll()
                }

                val prevKey = if (page == STARTING_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = response.results.map { pokemon ->
                    RemoteKeyEntity(
                        pokemonName = pokemon.name,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                val pokemonEntities = response.results.map { pokemon ->
                    PokemonEntity(
                        name = pokemon.name,
                        url = pokemon.url,
                        page = page
                    )
                }

                remoteKeyDao.insertAll(keys)
                pokemonDao.insertAll(pokemonEntities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            Timber.e(e, "Network error while fetching Pokemon list")
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Timber.e(e, "Error while fetching Pokemon list")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.name?.let { name ->
                remoteKeyDao.getRemoteKey(name)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { pokemon ->
            remoteKeyDao.getRemoteKey(pokemon.name)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PokemonEntity>
    ): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { pokemon ->
            remoteKeyDao.getRemoteKey(pokemon.name)
        }
    }
}


