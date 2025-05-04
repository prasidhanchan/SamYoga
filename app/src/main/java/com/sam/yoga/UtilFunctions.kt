package com.sam.yoga

import androidx.navigation.NavBackStackEntry
import com.sam.yoga.domain.navigation.Route
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

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
                Route.EditProfile::class.java.simpleName -> Route.EditProfile
                Route.RecentActivity::class.java.simpleName -> Route.RecentActivity
                Route.Saved::class.java.simpleName -> Route.Saved
                else -> Route.Home
            }
        }
    return screen
}

fun Long.formatChatTimestamp(): String {
    val date = Date(this)
    val format = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return format.format(date)
}

fun getGreetings(): String {
    val currentTime = LocalDateTime.now()

    return when (currentTime.hour) {
        in 0..11 -> {
            return "Good Morning"
        }

        in 12..16 -> {
            return "Good Afternoon"
        }

        else -> {
            return "Good Evening"
        }
    }
}

fun getImageResource(id: Int): Int {
    return when (id) {
        2131165264 -> R.drawable.adho_mukha_svanasana
        2131165265 -> R.drawable.ardha_chandrasana
        2131165270 -> R.drawable.baddha_konasana
        2131165323 -> R.drawable.natarajasana
        2131165356 -> R.drawable.trikonasana
        2131165357 -> R.drawable.utkata_konasana
        2131165358 -> R.drawable.veerabhadrasana
        2131165359 -> R.drawable.vrukshasana
        else -> R.drawable.ardha_chandrasana
    }
}