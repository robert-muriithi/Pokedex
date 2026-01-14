package com.robert.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robert.design_system.R
import com.robert.design_system.theme.PokemonTheme

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.oops_something_went_wrong)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Preview(name = "ErrorContent - Default", showBackground = true)
@Composable
private fun ErrorContentDefaultPreview() {
    PokemonTheme {
        Surface {
            ErrorContent(
                message = "Failed to load Pokémon data. Please check your internet connection.",
                onRetry = {}
            )
        }
    }
}

@Preview(name = "ErrorContent - Custom Title", showBackground = true)
@Composable
private fun ErrorContentCustomTitlePreview() {
    PokemonTheme {
        Surface {
            ErrorContent(
                title = "No Pokémon Found",
                message = "The Pokémon you're looking for doesn't exist in the Pokédex.",
                onRetry = {}
            )
        }
    }
}

@Preview(name = "ErrorContent - Network Error", showBackground = true)
@Composable
private fun ErrorContentNetworkPreview() {
    PokemonTheme {
        Surface {
            ErrorContent(
                title = "Connection Lost",
                message = "Unable to connect to the server. Please check your network connection and try again.",
                onRetry = {}
            )
        }
    }
}

