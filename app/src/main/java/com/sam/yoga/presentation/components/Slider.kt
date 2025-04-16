package com.sam.yoga.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Slider(
    state: PagerState,
    sliderImages: List<Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .padding(vertical = 30.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            state = state,
            userScrollEnabled = true,
            pageContent = { page ->
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = sliderImages[page]),
                            contentDescription = "ImageSlider",
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
            }
        )
        DotsIndicator(
            pageCount = state.pageCount,
            selectedIndex = state.currentPage
        )
    }
}

@Composable
private fun DotsIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    selectedIndex: Int
) {
    Row(
        modifier = modifier
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color = if (selectedIndex == page) Color.White else Color.Gray),
            )
        }
    }
}