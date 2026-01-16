package com.robert.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PokemonDetails(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val imageUrl: String,
    val types: ImmutableList<String>,
    val abilities: ImmutableList<String>,
    val stats: ImmutableList<PokemonStats>
)

@Immutable
data class PokemonStats(
    val name: String,
    val baseStat: Int,
    val effort: Int
)

