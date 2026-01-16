package com.robert.database.di

import android.content.Context
import androidx.room.Room
import com.robert.database.PokemonDatabase
import com.robert.database.dao.PokemonDao
import com.robert.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(database: PokemonDatabase): RemoteKeyDao {
        return database.remoteKeyDao()
    }
}
