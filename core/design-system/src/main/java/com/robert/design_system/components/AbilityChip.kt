package com.robert.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robert.design_system.theme.PokemonTheme

@Composable
fun AbilityChip(
    abilityName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = abilityName.replace("-", " ").replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(name = "AbilityChip - Single", showBackground = true)
@Composable
private fun AbilityChipSinglePreview() {
    PokemonTheme {
        Surface {
            AbilityChip(
                abilityName = "Overgrow",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(name = "AbilityChip - Multiple", showBackground = true)
@Composable
private fun AbilityChipMultiplePreview() {
    PokemonTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pokemon Abilities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AbilityChip("Static")
                    AbilityChip("Lightning Rod")
                    AbilityChip("Overgrow")
                    AbilityChip("Blaze")
                    AbilityChip("Torrent")
                    AbilityChip("Shield Dust")
                }
            }
        }
    }
}

