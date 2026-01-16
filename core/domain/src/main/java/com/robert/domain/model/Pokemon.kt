package com.robert.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String
)

