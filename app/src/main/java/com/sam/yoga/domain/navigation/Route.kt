package com.sam.yoga.domain.navigation

import com.sam.yoga.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val icon: Int = 0) {
    @Serializable
    data object Splash : Route()

    @Serializable
    data object Main : Route()

    @Serializable
    data object Home : Route(icon = R.drawable.home)

    @Serializable
    data class Scan(val poseName: String = "", val poseLevel: String? = null) : Route()

    @Serializable
    data object Session : Route(icon = R.drawable.session)

    @Serializable
    data object Explore : Route(icon = R.drawable.explore)

    @Serializable
    data object Profile : Route(icon = R.drawable.profile)

    companion object {
        val routes = listOf(Home, Session, Explore, Profile)
    }
}