package com.robert.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val imageUrl: String,
    val types: ImmutableList<String>,
    val abilities: ImmutableList<String>,
    val stats: ImmutableList<PokemonStat>
)

@Immutable
data class PokemonStat(
    val name: String,
    val baseStat: Int,
    val effort: Int
)

