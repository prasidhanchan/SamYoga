package com.sam.yoga.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sam.yoga.presentation.components.AppBar
import com.sam.yoga.presentation.components.PoseCard
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun RecentActivityScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel,
    navHostController: NavHostController,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

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

        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
            items(items = uiState.value.activities) { pose ->
                PoseCard(
                    poseName = pose.name,
                    level = pose.level,
                    time = pose.time,
                    image = painterResource(id = pose.image),
                    onClick = { }
                )
            }
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