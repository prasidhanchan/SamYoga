package com.sam.yoga.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sam.yoga.R
import com.sam.yoga.presentation.components.ProfileCard
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.OffBlack
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onLogoutClick: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Profile",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Surface(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 20.dp)
                .size(100.dp),
            shape = CircleShape,
            color = OffBlack
        ) {
            Image(
                painter = painterResource(
                    id = if (uiState.value.user?.gender == "Female")
                        R.drawable.avatar_female else
                        R.drawable.avatar_male
                ),
                contentDescription = "Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.22f)
            )
        }

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
                onClick = { }
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
                    viewModel.logout(
                        onSuccess = onLogoutClick
                    )
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
            viewModel = MainViewModel(),
            onLogoutClick = { }
        )
    }
}