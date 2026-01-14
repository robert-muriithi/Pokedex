package com.robert.design_system.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robert.design_system.theme.PokemonTheme
import com.robert.design_system.theme.StatColors

@Composable
fun StatBar(
    modifier: Modifier = Modifier,
    statName: String,
    statValue: Int,
    maxValue: Int = 255,
    animated: Boolean = true
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = if (animated) 1000 else 0),
        label = "stat_progress"
    )

    LaunchedEffect(statValue) {
        progress = statValue.toFloat() / maxValue
    }

    val statColor = StatColors.getColor(statName)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatStatName(statName),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = statValue.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = statColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = statColor,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )
    }
}

private fun formatStatName(statName: String): String {
    return when (statName.lowercase().replace("-", " ")) {
        "hp" -> "HP"
        "attack" -> "Attack"
        "defense" -> "Defense"
        "special attack" -> "Sp. Atk"
        "special defense" -> "Sp. Def"
        "speed" -> "Speed"
        else -> statName.replaceFirstChar { it.uppercase() }
    }
}

@Preview(name = "StatBar - HP", showBackground = true)
@Composable
private fun StatBarHPPreview() {
    PokemonTheme {
        Surface {
            StatBar(
                statName = "hp",
                statValue = 45,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "StatBar - Attack", showBackground = true)
@Composable
private fun StatBarAttackPreview() {
    PokemonTheme {
        Surface {
            StatBar(
                statName = "attack",
                statValue = 85,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "StatBar - All Stats", showBackground = true)
@Composable
private fun StatBarAllStatsPreview() {
    PokemonTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                StatBar(statName = "hp", statValue = 45)
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(statName = "attack", statValue = 85)
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(statName = "defense", statValue = 50)
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(statName = "special-attack", statValue = 65)
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(statName = "special-defense", statValue = 65)
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(statName = "speed", statValue = 90)
            }
        }
    }
}

