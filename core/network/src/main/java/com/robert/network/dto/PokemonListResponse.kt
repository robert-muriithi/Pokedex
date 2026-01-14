package com.robert.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonDto>
)

data class PokemonDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)