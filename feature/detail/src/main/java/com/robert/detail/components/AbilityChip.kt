package com.robert.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.robert.design_system.components.AbilityChip

@Composable
fun AbilityChip(
    ability: String,
    modifier: Modifier = Modifier
) {
    AbilityChip(
        abilityName = ability,
        modifier = modifier
    )
}

