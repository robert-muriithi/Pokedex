package com.robert.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robert.data.local.dao.PokemonDao
import com.robert.data.local.dao.RemoteKeyDao
import com.robert.data.local.entity.PokemonEntity
import com.robert.data.local.entity.RemoteKeyEntity

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
