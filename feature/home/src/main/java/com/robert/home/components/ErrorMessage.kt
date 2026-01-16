package com.robert.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.robert.design_system.components.ErrorContent
import com.robert.home.R

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
        title = stringResource(R.string.failed_to_load_pok_mon)
    )
}

