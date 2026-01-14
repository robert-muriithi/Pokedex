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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robert.design_system.theme.PokemonTheme
import com.robert.design_system.theme.TypeColors

@Composable
fun TypeChip(
    typeName: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = TypeColors.getColor(typeName)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = typeName.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(name = "TypeChip - Single", showBackground = true)
@Composable
private fun TypeChipSinglePreview() {
    PokemonTheme {
        Surface {
            TypeChip(
                typeName = "Fire",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(name = "TypeChip - All Types", showBackground = true)
@Composable
private fun TypeChipAllTypesPreview() {
    PokemonTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pokemon Types",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TypeChip("Normal")
                    TypeChip("Fire")
                    TypeChip("Water")
                    TypeChip("Electric")
                    TypeChip("Grass")
                    TypeChip("Ice")
                    TypeChip("Fighting")
                    TypeChip("Poison")
                    TypeChip("Ground")
                    TypeChip("Flying")
                    TypeChip("Psychic")
                    TypeChip("Bug")
                    TypeChip("Rock")
                    TypeChip("Ghost")
                    TypeChip("Dragon")
                    TypeChip("Dark")
                    TypeChip("Steel")
                    TypeChip("Fairy")
                }
            }
        }
    }
}

