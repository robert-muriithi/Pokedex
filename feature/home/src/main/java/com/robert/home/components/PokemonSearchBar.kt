package com.robert.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.robert.design_system.theme.PokemonTheme
import com.robert.domain.model.Pokemon
import com.robert.home.R
import com.robert.home.SearchUiState
import com.robert.home.UiText
import java.util.Locale

@Composable
fun PokemonSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    searchState: SearchUiState,
    onPokemonClick: (Pokemon) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = {
                Text(stringResource(R.string.search_pok_mon_by_name_or_id))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        AnimatedVisibility(
            visible = query.isNotEmpty() && searchState !is SearchUiState.Idle,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchResultContent(
                searchState = searchState,
                onPokemonClick = onPokemonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun SearchResultContent(
    searchState: SearchUiState,
    onPokemonClick: (Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        when (searchState) {
            is SearchUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            }

            is SearchUiState.Success -> {
                SearchResultItem(
                    pokemon = searchState.pokemon,
                    onClick = { onPokemonClick(searchState.pokemon) }
                )
            }

            is SearchUiState.Error -> {
                val errorMessage = when (val text = searchState.message) {
                    is UiText.StringResource -> stringResource(text.resId, *text.args.toTypedArray())
                    is UiText.DynamicString -> text.value
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is SearchUiState.Idle -> {}
        }
    }
}

@Composable
private fun SearchResultItem(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember(pokemon.imageUrl) {
        ImageRequest.Builder(context)
            .data(pokemon.imageUrl)
            .crossfade(true)
            .build()
    }

    val formattedName = remember(pokemon.name) {
        pokemon.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageRequest,
            contentDescription = pokemon.name,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formattedName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(name = "PokemonSearchBar - Empty", showBackground = true)
@Composable
private fun PokemonSearchBarEmptyPreview() {
    PokemonTheme {
        Surface {
            PokemonSearchBar(
                query = "",
                onQueryChange = {},
                searchState = SearchUiState.Idle,
                onPokemonClick = {},
                onClearClick = {}
            )
        }
    }
}

@Preview(name = "PokemonSearchBar - With Query", showBackground = true)
@Composable
private fun PokemonSearchBarWithQueryPreview() {
    PokemonTheme {
        Surface {
            PokemonSearchBar(
                query = "pikachu",
                onQueryChange = {},
                searchState = SearchUiState.Idle,
                onPokemonClick = {},
                onClearClick = {}
            )
        }
    }
}

@Preview(name = "PokemonSearchBar - Loading", showBackground = true)
@Composable
private fun PokemonSearchBarLoadingPreview() {
    PokemonTheme {
        Surface {
            PokemonSearchBar(
                query = "charmander",
                onQueryChange = {},
                searchState = SearchUiState.Loading,
                onPokemonClick = {},
                onClearClick = {}
            )
        }
    }
}

@Preview(name = "PokemonSearchBar - Success", showBackground = true)
@Composable
private fun PokemonSearchBarSuccessPreview() {
    PokemonTheme {
        Surface {
            PokemonSearchBar(
                query = "bulbasaur",
                onQueryChange = {},
                searchState = SearchUiState.Success(
                    Pokemon(
                        id = 1,
                        name = "bulbasaur",
                        url = "",
                        imageUrl = ""
                    )
                ),
                onPokemonClick = {},
                onClearClick = {}
            )
        }
    }
}

@Preview(name = "PokemonSearchBar - Error", showBackground = true)
@Composable
private fun PokemonSearchBarErrorPreview() {
    PokemonTheme {
        Surface {
            PokemonSearchBar(
                query = "unknown",
                onQueryChange = {},
                searchState = SearchUiState.Error(
                    message = UiText.StringResource(R.string.pok_mon_not_found, listOf("unknown"))
                ),
                onPokemonClick = {},
                onClearClick = {}
            )
        }
    }
}

