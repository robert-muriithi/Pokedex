package com.robert.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.robert.design_system.components.TypeChip as DesignSystemTypeChip

@Composable
fun TypeChip(
    type: String,
    modifier: Modifier = Modifier
) {
    DesignSystemTypeChip(
        typeName = type,
        modifier = modifier
    )
}

