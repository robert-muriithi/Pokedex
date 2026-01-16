package com.robert.data.mapper

import com.robert.database.entity.PokemonEntity
import com.robert.domain.model.Pokemon
import com.robert.domain.model.PokemonDetails
import com.robert.domain.model.PokemonStats
import com.robert.network.dto.PokemonDetailsResponse
import com.robert.network.util.NetworkConstants
import kotlinx.collections.immutable.toImmutableList

fun PokemonEntity.toDomain(): Pokemon {
    val pokemonId = url.trimEnd('/').split("/").last().toIntOrNull() ?: 0
    return Pokemon(
        id = pokemonId,
        name = name,
        url = url,
        imageUrl = "${NetworkConstants.POKEMON_IMAGE_BASE_URL}$pokemonId.png"
    )
}

fun PokemonDetailsResponse.toDomain(): PokemonDetails {
    return PokemonDetails(
        id = id,
        name = name,
        height = height,
        weight = weight,
        baseExperience = baseExperience,
        imageUrl = sprites.other?.officialArtwork?.frontDefault
            ?: sprites.frontDefault
            ?: "${NetworkConstants.POKEMON_IMAGE_BASE_URL}${id}.png",
        types = types.map { it.type.name.replaceFirstChar { char -> char.uppercase() } }.toImmutableList(),
        abilities = abilities.map { it.ability.name.replaceFirstChar { char -> char.uppercase() } }.toImmutableList(),
        stats = stats.map { stat ->
            PokemonStats(
                name = stat.stat.name.replaceFirstChar { char -> char.uppercase() },
                baseStat = stat.baseStat,
                effort = stat.effort
            )
        }.toImmutableList()
    )
}

