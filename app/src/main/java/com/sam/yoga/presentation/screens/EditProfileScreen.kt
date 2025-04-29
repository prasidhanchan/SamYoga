package com.sam.yoga.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sam.yoga.R
import com.sam.yoga.presentation.components.AppBar
import com.sam.yoga.presentation.components.ProfileImage
import com.sam.yoga.presentation.components.SamYogaButton
import com.sam.yoga.presentation.components.SamYogaTextBox2
import com.sam.yoga.presentation.theme.OffBlack
import com.sam.yoga.presentation.theme.SamYogaTheme
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel,
    navHostController: NavHostController,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val genders = listOf("Male", "Female", "Other")
    var selectedName by remember { mutableStateOf(uiState.value.user?.name ?: "") }
    var selectedGender by remember { mutableStateOf(uiState.value.user?.gender ?: "") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            title = "Edit Profile",
            isBackVisible = true,
            onBackClick = { navHostController.popBackStack() }
        )

        ProfileImage(gender = selectedGender)
        SamYogaTextBox2(
            value = selectedName,
            onValueChange = { selectedName = it },
            leadingIcon = R.drawable.name,
            label = "Name"
        )
        SamYogaTextBox2(
            value = uiState.value.user?.email ?: "",
            onValueChange = { },
            enable = false,
            leadingIcon = R.drawable.email,
            label = "Email"
        )

        // Gender
        Text(
            text = "Gender",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            genders.forEach { gender ->
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = gender,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Surface(
                        modifier = Modifier.size(20.dp),
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 2.dp,
                            color = if (selectedGender == gender) Color.White else OffBlack
                        ),
                        onClick = { selectedGender = gender },
                        content = { }
                    )
                }
            }
        }

        SamYogaButton(
            text = "Update",
            onClick = {
                viewModel.updateUserProfile(
                    name = selectedName,
                    gender = selectedGender,
                    onSuccess = {
                        viewModel.getUserProfile()
                        scope.launch {
                            Toast.makeText(
                                context,
                                "Profile updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        navHostController.popBackStack()
                    },
                    onError = { error ->
                        scope.launch {
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }
                    }
                )
            },
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    SamYogaTheme {
        EditProfileScreen(
            innerPadding = PaddingValues(),
            viewModel = MainViewModel(),
            navHostController = NavHostController(LocalContext.current)
        )
    }
}