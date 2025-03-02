package com.sam.yoga.domain.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash

    @Serializable
    data object Home

    @Serializable
    data class Scan(val poseName: String)
}