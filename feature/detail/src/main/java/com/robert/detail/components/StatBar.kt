package com.robert.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.robert.design_system.components.StatBar as DesignSystemStatBar

@Composable
fun StatBar(
    modifier: Modifier = Modifier,
    statName: String,
    statValue: Int,
    maxValue: Int = 255
) {
    DesignSystemStatBar(
        statName = statName,
        statValue = statValue,
        maxValue = maxValue,
        modifier = modifier,
        animated = true
    )
}

