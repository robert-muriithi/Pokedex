package com.robert.data.di

import android.content.Context
import androidx.room.Room
import com.robert.data.local.database.PokemonDatabase
import com.robert.data.repository.PokemonRepositoryImpl
import com.robert.domain.repository.PokemonRepository
import com.robert.network.api.PokemonApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            PokemonDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonRepository(
        apiService: PokemonApiService,
        database: PokemonDatabase
    ): PokemonRepository {
        return PokemonRepositoryImpl(apiService, database)
    }
}

