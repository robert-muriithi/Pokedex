package com.robert.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robert.data.local.entity.PokemonEntity

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY page ASC")
    fun getAllPokemon(): PagingSource<Int, PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()
    
    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' LIMIT 1")
    suspend fun searchPokemon(query: String): PokemonEntity?
    
    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchPokemonList(query: String): List<PokemonEntity>
}

