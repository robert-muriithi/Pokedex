package com.robert.design_system.theme

import androidx.compose.ui.graphics.Color

val PokemonRed = Color(0xFFEE1515)
val PokemonRedLight = Color(0xFFFF6B6B)
val PokemonRedDark = Color(0xFFB30000)
val PokemonYellow = Color(0xFFFFCB05)
val PokemonYellowLight = Color(0xFFFFE066)
val PokemonBlue = Color(0xFF2A75BB)
val PokemonBlueLight = Color(0xFF5BA3E7)
val PokemonBlueDark = Color(0xFF1A4A7A)
val BackgroundLight = Color(0xFFFAFAFA)
val BackgroundDark = Color(0xFF1A1A1A)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF2A2A2A)

object TypeColors {
    val Normal = Color(0xFFA8A878)
    val Fire = Color(0xFFFF4422)
    val Water = Color(0xFF3399FF)
    val Electric = Color(0xFFFFCC33)
    val Grass = Color(0xFF77CC55)
    val Ice = Color(0xFF66CCFF)
    val Fighting = Color(0xFFBB5544)
    val Poison = Color(0xFFAA5599)
    val Ground = Color(0xFFDDBB55)
    val Flying = Color(0xFF8899FF)
    val Psychic = Color(0xFFFF5599)
    val Bug = Color(0xFFAABB22)
    val Rock = Color(0xFFBBAA66)
    val Ghost = Color(0xFF6666BB)
    val Dragon = Color(0xFF7766EE)
    val Dark = Color(0xFF775544)
    val Steel = Color(0xFFAAAABB)
    val Fairy = Color(0xFFEE99EE)

    fun getColor(typeName: String): Color {
        return when (typeName.lowercase()) {
            "normal" -> Normal
            "fire" -> Fire
            "water" -> Water
            "electric" -> Electric
            "grass" -> Grass
            "ice" -> Ice
            "fighting" -> Fighting
            "poison" -> Poison
            "ground" -> Ground
            "flying" -> Flying
            "psychic" -> Psychic
            "bug" -> Bug
            "rock" -> Rock
            "ghost" -> Ghost
            "dragon" -> Dragon
            "dark" -> Dark
            "steel" -> Steel
            "fairy" -> Fairy
            else -> Normal
        }
    }
}

object StatColors {
    val HP = Color(0xFFFF5959)
    val Attack = Color(0xFFF5AC78)
    val Defense = Color(0xFFFAE078)
    val SpAttack = Color(0xFF9DB7F5)
    val SpDefense = Color(0xFFA7DB8D)
    val Speed = Color(0xFFFA92B2)

    fun getColor(statName: String): Color {
        return when (statName.lowercase().replace("-", "").replace(" ", "")) {
            "hp" -> HP
            "attack" -> Attack
            "defense" -> Defense
            "specialattack", "spattack" -> SpAttack
            "specialdefense", "spdefense" -> SpDefense
            "speed" -> Speed
            else -> Color.Gray
        }
    }
}

