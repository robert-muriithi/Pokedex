package com.robert.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.robert.design_system.components.ErrorContent

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorContent(
        message = message,
        onRetry = onRetry,
        modifier = modifier,
        title = "Failed to load Pokémon"
    )
}

