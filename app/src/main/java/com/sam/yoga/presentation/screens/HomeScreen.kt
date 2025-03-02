package com.sam.yoga.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sam.yoga.R
import com.sam.yoga.domain.models.YogaPose
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.components.PoseCard
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    val poses = listOf(
        YogaPose(
            name = "Ardha Chandrasana",
            description = "",
            level = "Beginner",
            time = 2L
        ),
        YogaPose(
            name = "Adho Mukha Svanasana",
            description = "",
            level = "Intermediate",
            time = 2L
        ),
        YogaPose(
            name = "Baddha Konasana",
            description = "",
            level = "Professional",
            time = 2L
        ),
        YogaPose(
            name = "Natarajasana",
            description = "",
            level = "Beginner",
            time = 2L
        ),
        YogaPose(
            name = "Trikonasana",
            description = "",
            level = "Professional",
            time = 2L
        ),
        YogaPose(
            name = "Utkata Konasana",
            description = "",
            level = "Professional",
            time = 2L
        ),
        YogaPose(
            name = "Veerabhadrasana",
            description = "",
            level = "Intermediate",
            time = 2L
        ),
        YogaPose(
            name = "Vrukshasana",
            description = "",
            level = "Professional",
            time = 2L
        )
    )

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        poses.forEach { pose ->
            PoseCard(
                poseName = pose.name,
                level = pose.level,
                time = pose.time,
                color = when(pose.level) {
                    "Beginner" -> Color(0xFF1DB1F5)
                    "Intermediate" -> Color(0xFFF5AAAA)
                    else -> Color(0xFFE8A33C)
                },
                image = painterResource(R.drawable.scan),
                onClick = { navHostController.navigate(Route.Scan(poseName = pose.name)) },
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0XFFFFFFFF
)
@Composable
private fun HomeScreenPreview() {
    SamYogaTheme {
        HomeScreen(
            innerPadding = PaddingValues(),
            navHostController = rememberNavController()
        )
    }
}