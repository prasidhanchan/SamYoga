package com.sam.yoga.domain.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sam.yoga.presentation.screens.SplashScreen
import com.sam.yoga.presentation.screens.auth.AuthViewModel
import com.sam.yoga.presentation.screens.auth.LoginScreen
import com.sam.yoga.presentation.screens.auth.SignUpScreen

@Composable
fun SamYogaNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Splash
        ) {
            composable<Route.Splash> {
                val currentUser = FirebaseAuth.getInstance().currentUser

                SplashScreen(
                    innerPadding = innerPadding,
                    currentUser = currentUser,
                    navHostController = navController
                )
            }

            composable<Route.SignUp> {
                val viewModel: AuthViewModel = viewModel()

                SignUpScreen(
                    innerPadding = innerPadding,
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable<Route.Login> {
                val viewModel: AuthViewModel = viewModel()

                LoginScreen(
                    innerPadding = innerPadding,
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable<Route.Main> {
                InnerNavigation(
                    onLogoutClick = {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.Home) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}