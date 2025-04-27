package com.sam.yoga.domain.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sam.yoga.getCurrentRoute
import com.sam.yoga.presentation.components.BottomBar
import com.sam.yoga.presentation.screens.ExploreScreen
import com.sam.yoga.presentation.screens.HomeScreen
import com.sam.yoga.presentation.screens.MainViewModel
import com.sam.yoga.presentation.screens.ProfileScreen
import com.sam.yoga.presentation.screens.ScanScreen
import com.sam.yoga.presentation.screens.SessionScreen

@Composable
fun InnerNavigation(onLogoutClick: () -> Unit) {
    val navController = rememberNavController()
    val navaBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navaBackStackEntry.value?.getCurrentRoute()

    val viewModel: MainViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = Route.Home,
            navController = navController,
            modifier = Modifier.statusBarsPadding()
        ) {
            composable<Route.Home> {
                HomeScreen(
                    innerPadding = innerPadding,
                    navHostController = navController
                )
            }
            composable<Route.Scan> { backStack ->
                val poseName = backStack.toRoute<Route.Scan>().poseName
                val poseLevel = backStack.toRoute<Route.Scan>().poseLevel

                ScanScreen(
                    poseName = poseName,
                    poseLevel = poseLevel,
                    navHostController = navController,
                    viewModel = viewModel
                )
            }
            composable<Route.Session> {
                SessionScreen(navController = navController)
            }
            composable<Route.Explore> {
                ExploreScreen()
            }
            composable<Route.Profile> {
                ProfileScreen(
                    viewModel = viewModel,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}