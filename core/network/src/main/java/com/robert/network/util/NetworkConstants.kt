package com.robert.network.util

import com.robert.network.BuildConfig

object NetworkConstants {
    val POKEMON_IMAGE_BASE_URL: String
        get() = BuildConfig.POKEMON_IMAGE_URL

    val POKEMON_API_BASE_URL: String
        get() = BuildConfig.POKEMON_API_URL
}
