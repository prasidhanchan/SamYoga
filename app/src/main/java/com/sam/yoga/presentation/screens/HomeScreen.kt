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
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.components.ActionIcon
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(20.dp)
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
        Spacer(modifier = Modifier.height(50.dp))
        Spacer(modifier = Modifier.weight(1f))
        ActionIcon(
            text = stringResource(R.string.scan_now),
            icon = painterResource(R.drawable.scan),
            onClick = { navHostController.navigate(Route.Search) },
        )
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