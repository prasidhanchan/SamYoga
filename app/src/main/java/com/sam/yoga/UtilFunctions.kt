package com.sam.yoga

import androidx.navigation.NavBackStackEntry
import com.sam.yoga.domain.navigation.Route

fun NavBackStackEntry.getCurrentRoute(): Route {
    var screen: Route = Route.Home

    this.destination.route
        ?.substringBefore("?")
        ?.substringBefore("/")
        ?.substringAfterLast(".")
        ?.let { route ->
            screen = when (route) {
                Route.Splash::class.java.simpleName -> Route.Splash
                Route.Home::class.java.simpleName -> Route.Home
                Route.Scan::class.java.simpleName -> Route.Scan()
                Route.Session::class.java.simpleName -> Route.Session
                Route.Explore::class.java.simpleName -> Route.Explore
                Route.Profile::class.java.simpleName -> Route.Profile
                else -> Route.Home
            }
        }
    return screen
}