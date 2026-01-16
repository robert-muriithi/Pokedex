package com.robert.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robert.database.dao.PokemonDao
import com.robert.database.dao.RemoteKeyDao
import com.robert.database.entity.PokemonEntity
import com.robert.database.entity.RemoteKeyEntity


@Database(
    entities = [PokemonEntity::class, RemoteKeyEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "pokemon_database"
    }
}
