package com.robert.detail.util

import androidx.compose.ui.graphics.Color

fun getTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "fire" -> Color(0xFFFF4422)
        "water" -> Color(0xFF3399FF)
        "grass" -> Color(0xFF77CC55)
        "electric" -> Color(0xFFFFCC33)
        "psychic" -> Color(0xFFFF55AA)
        "fighting" -> Color(0xFFBB5544)
        "normal" -> Color(0xFFAAAAAA)
        "ground" -> Color(0xFFDDBB55)
        "poison" -> Color(0xFFAA5599)
        "rock" -> Color(0xFFBBAA66)
        "bug" -> Color(0xFFAABB22)
        "ghost" -> Color(0xFF6666BB)
        "steel" -> Color(0xFFAAAABB)
        "ice" -> Color(0xFF66CCFF)
        "dragon" -> Color(0xFF7766EE)
        "dark" -> Color(0xFF775544)
        "fairy" -> Color(0xFFEE99EE)
        "flying" -> Color(0xFF8899FF)
        else -> Color.Gray
    }
}
