package com.sam.yoga.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sam.yoga.getImageUrl
import com.sam.yoga.presentation.components.AppBar
import com.sam.yoga.presentation.components.Loader
import com.sam.yoga.presentation.components.PoseCard
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun RecentActivityScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel,
    navHostController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getUserActivities()
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            title = "Recent Activity",
            isBackVisible = true,
            onBackClick = { navHostController.popBackStack() }
        )

        if (uiState.activities.isNotEmpty() && !uiState.loading) {
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
                items(items = uiState.activities) { pose ->
                    PoseCard(
                        poseName = pose.name,
                        level = pose.level,
                        time = pose.time,
                        image = getImageUrl(pose.image),
                        onClick = { }
                    )
                }
            }
        } else if (uiState.activities.isEmpty() && !uiState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent activities!",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        } else {
            Loader()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentActivityScreenPreview() {
    SamYogaTheme {
        RecentActivityScreen(
            innerPadding = PaddingValues(),
            viewModel = MainViewModel(),
            navHostController = NavHostController(LocalContext.current)
        )
    }
}