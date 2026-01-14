package com.robert.navigation

import androidx.navigation3.runtime.NavKey
import com.robert.domain.model.Pokemon
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {

    @Serializable
    data object Home : Screen

    @Serializable
    data class Details(val pokemon: Pokemon) : Screen
}