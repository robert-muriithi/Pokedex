package com.robert.data.mapper

import com.robert.data.local.entity.PokemonEntity
import com.robert.domain.model.Pokemon
import com.robert.domain.model.PokemonDetail
import com.robert.domain.model.PokemonStat
import com.robert.network.dto.PokemonDetailResponse
import kotlinx.collections.immutable.toImmutableList

fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        name = name,
        url = url
    )
}

fun PokemonDetailResponse.toDomain(): PokemonDetail {
    return PokemonDetail(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        imageUrl = sprites.other?.officialArtwork?.frontDefault
            ?: sprites.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        types = types.map { it.type.name.replaceFirstChar { char -> char.uppercase() } }.toImmutableList(),
        abilities = abilities.map { it.ability.name.replaceFirstChar { char -> char.uppercase() } }.toImmutableList(),
        stats = stats.map { stat ->
            PokemonStat(
                name = stat.stat.name.replaceFirstChar { char -> char.uppercase() },
                baseStat = stat.baseStat,
                effort = stat.effort
            )
        }.toImmutableList()
    )
}

