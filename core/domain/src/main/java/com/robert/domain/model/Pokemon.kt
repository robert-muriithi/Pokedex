package com.robert.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Pokemon(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/').split("/").last().toIntOrNull() ?: 0

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

