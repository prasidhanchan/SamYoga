package com.sam.yoga.presentation.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.theme.SamYogaTheme
import kotlinx.coroutines.delay
import com.sam.yoga.R

@Composable
fun SplashScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    val animatable = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1.2f,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        )
        delay(1000L)
        navHostController.navigate(Route.Home) {
            popUpTo(Route.Splash) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SamYogaTheme {
        SplashScreen(
            innerPadding = PaddingValues(),
            navHostController = rememberNavController()
        )
    }
}