package com.robert.home.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

fun extractDominantColor(drawable: Drawable?): Color {
    val bitmap = (drawable as? BitmapDrawable)?.bitmap ?: return Color(0xFFE0E0E0)
    return extractDominantColor(bitmap)
}

fun extractDominantColor(bitmap: Bitmap): Color {
    return try {
        val palette = Palette.from(bitmap).generate()
        val color = palette.vibrantSwatch?.rgb
            ?: palette.lightVibrantSwatch?.rgb
            ?: palette.dominantSwatch?.rgb
            ?: 0xFFE0E0E0.toInt()
        Color(color)
    } catch (_: Exception) {
        Color(0xFFE0E0E0)
    }
}


