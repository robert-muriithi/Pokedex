package com.robert.network.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("base_experience")
    val baseExperience: Int,
    @SerializedName("sprites")
    val sprites: SpritesDto,
    @SerializedName("types")
    val types: List<TypeSlotDto>,
    @SerializedName("abilities")
    val abilities: List<AbilitySlotDto>,
    @SerializedName("stats")
    val stats: List<StatDto>
)

data class SpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("other")
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class TypeSlotDto(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: TypeDto
)

data class TypeDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class AbilitySlotDto(
    @SerializedName("ability")
    val ability: AbilityDto,
    @SerializedName("is_hidden")
    val isHidden: Boolean
)

data class AbilityDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class StatDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    @SerializedName("effort")
    val effort: Int,
    @SerializedName("stat")
    val stat: StatNameDto
)

data class StatNameDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

