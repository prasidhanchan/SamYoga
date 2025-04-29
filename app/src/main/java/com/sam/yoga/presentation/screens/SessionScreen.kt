package com.sam.yoga.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sam.yoga.R
import com.sam.yoga.domain.Util.poses
import com.sam.yoga.domain.models.Session
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.components.AppBar
import com.sam.yoga.presentation.components.SessionCard
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun SessionScreen(navController: NavHostController) {
    val beginnerPoses = poses.filter { pose -> pose.level == "Beginner" }
    val intermediatePoses = poses.filter { pose -> pose.level == "Intermediate" }
    val advancedPoses = poses.filter { pose -> pose.level == "Advanced" }
    val levels = listOf(
        Session(
            level = "Beginner",
            poses = beginnerPoses,
            image = R.drawable.ardha_chandrasana,
            time = 22L
        ),
        Session(
            level = "Intermediate",
            poses = intermediatePoses,
            image = R.drawable.veerabhadrasana,
            time = 30L
        ),
        Session(
            level = "Advanced",
            poses = advancedPoses,
            image = R.drawable.utkata_konasana,
            time = 40L
        )
    )

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AppBar(title = "Start Session")
        Box(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.session_image),
                contentDescription = "Start Session"
            )
        }
        levels.forEach { session ->
            SessionCard(
                poseLevel = session.level,
                image = session.image,
                time = session.time,
                onStartClick = {
                    navController.navigate(
                        Route.Scan(
                            poseName = session.poses.first().name,
                            poseLevel = session.level
                        )
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionScreenPreview() {
    SamYogaTheme {
        SessionScreen(navController = rememberNavController())
    }
}