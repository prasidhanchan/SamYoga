package com.sam.yoga.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.sam.yoga.R
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.components.AppBar
import com.sam.yoga.presentation.components.ProfileCard
import com.sam.yoga.presentation.components.ProfileImage
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel,
    onLogoutClick: () -> Unit,
    navHostController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AppBar(title = "Profile")
        ProfileImage(gender = uiState.value.user?.gender)

        Text(
            text = uiState.value.user?.name ?: "",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandColor
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = uiState.value.user?.email ?: "",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ProfileCard(
                icon = R.drawable.edit,
                title = "Edit Profile",
                onClick = { navHostController.navigate(Route.EditProfile) }
            )
            ProfileCard(
                icon = R.drawable.recent_activity,
                title = "Recent Activity",
                onClick = { navHostController.navigate(Route.RecentActivity) }
            )
            ProfileCard(
                icon = R.drawable.saved,
                title = "Saved",
                onClick = { }
            )
            ProfileCard(
                icon = R.drawable.logout,
                title = "Logout",
                onClick = {
                    viewModel.logout(onSuccess = onLogoutClick)
                },
                isLogout = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    SamYogaTheme {
        ProfileScreen(
            innerPadding = PaddingValues(),
            viewModel = MainViewModel(),
            onLogoutClick = { },
            navHostController = NavHostController(LocalContext.current)
        )
    }
}