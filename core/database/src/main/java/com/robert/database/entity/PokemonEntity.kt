package com.robert.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey
    val name: String,
    val url: String,
    val page: Int
)
