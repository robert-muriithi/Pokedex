package com.robert.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val pokemonName: String,
    val prevKey: Int?,
    val nextKey: Int?
)

