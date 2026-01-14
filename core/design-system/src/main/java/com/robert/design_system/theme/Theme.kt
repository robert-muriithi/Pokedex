package com.robert.design_system.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PokemonRedLight,
    onPrimary = Color.White,
    primaryContainer = PokemonRedDark,
    onPrimaryContainer = Color.White,
    secondary = PokemonYellow,
    onSecondary = Color.Black,
    secondaryContainer = PokemonYellowLight,
    onSecondaryContainer = Color.Black,
    tertiary = PokemonBlueLight,
    onTertiary = Color.White,
    tertiaryContainer = PokemonBlueDark,
    onTertiaryContainer = Color.White,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF3A3A3A),
    onSurfaceVariant = Color(0xFFCACACA),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = PokemonRed,
    onPrimary = Color.White,
    primaryContainer = PokemonRedLight,
    onPrimaryContainer = Color.White,
    secondary = PokemonYellow,
    onSecondary = Color.Black,
    secondaryContainer = PokemonYellowLight,
    onSecondaryContainer = Color.Black,
    tertiary = PokemonBlue,
    onTertiary = Color.White,
    tertiaryContainer = PokemonBlueLight,
    onTertiaryContainer = Color.White,
    background = BackgroundLight,
    onBackground = Color(0xFF1A1A1A),
    surface = SurfaceLight,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF5A5A5A),
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun PokemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PokedexTypography,
        content = content
    )
}

