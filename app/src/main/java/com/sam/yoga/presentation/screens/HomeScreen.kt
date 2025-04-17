package com.sam.yoga.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.sam.yoga.domain.Util.poses
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.components.PoseCard
import com.sam.yoga.presentation.components.Slider
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.PurpleColor
import com.sam.yoga.presentation.theme.RedColor
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    val sliderImages = listOf(R.drawable.carousel2, R.drawable.carousel1)
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { sliderImages.size }
    )

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 10.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
        )
        Slider(state = pagerState, sliderImages = sliderImages)
        Text(
            text = "Explore poses",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            poses.forEach { pose ->
                PoseCard(
                    poseName = pose.name,
                    level = pose.level,
                    time = pose.time,
                    color = when (pose.level) {
                        "Beginner" -> BrandColor
                        "Intermediate" -> PurpleColor
                        else -> RedColor
                    },
                    image = painterResource(id = pose.image),
                    onClick = { navHostController.navigate(Route.Scan(poseName = pose.name)) },
                )
            }
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