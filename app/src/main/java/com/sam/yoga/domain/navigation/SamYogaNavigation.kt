package com.sam.yoga.domain.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sam.yoga.presentation.screens.HomeScreen
import com.sam.yoga.presentation.screens.MainViewModel
import com.sam.yoga.presentation.screens.ScanScreen
import com.sam.yoga.presentation.screens.SplashScreen

@Composable
fun SamYogaNavigation(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash
        ) {
            composable<Route.Splash> {
                SplashScreen(
                    innerPadding = innerPadding,
                    navHostController = navController
                )
            }
            composable<Route.Home> {
                HomeScreen(
                    innerPadding = innerPadding,
                    navHostController = navController
                )
            }
            composable<Route.Search> {
                ScanScreen(
                    viewModel = viewModel,
                    navHostController = navController
                )
            }
        }
    }
}