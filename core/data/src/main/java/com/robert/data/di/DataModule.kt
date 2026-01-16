package com.robert.data.di

import com.robert.data.repository.PokemonRepositoryImpl
import com.robert.database.PokemonDatabase
import com.robert.domain.repository.PokemonRepository
import com.robert.network.api.PokemonApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    fun providePokemonRepository(
        apiService: PokemonApiService,
        database: PokemonDatabase
    ): PokemonRepository {
        return PokemonRepositoryImpl(apiService, database)
    }
}

