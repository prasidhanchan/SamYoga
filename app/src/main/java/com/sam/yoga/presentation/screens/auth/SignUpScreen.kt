package com.sam.yoga.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sam.yoga.R
import com.sam.yoga.domain.navigation.Route
import com.sam.yoga.presentation.components.SamYogaButton
import com.sam.yoga.presentation.components.SamYogaTextBox
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.OffBlack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    innerPadding: PaddingValues,
    viewModel: AuthViewModel,
    navController: NavController,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BrandColor
    ) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painterResource(id = R.drawable.signup_image),
                contentDescription = "Login",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .scale(1.05f)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "SignUp",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "Breathe Deeply. Sleep Peacefully. \nLive Mindfully.",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            bottom = 20.dp
                        )
                        .fillMaxWidth()
                )
                SamYogaTextBox(
                    value = uiState.value.name,
                    onValueChange = viewModel::setName,
                    placeholder = "Enter your full name",
                    leadingIcon = R.drawable.name
                )
                SamYogaTextBox(
                    value = uiState.value.email,
                    onValueChange = viewModel::setEmail,
                    placeholder = "Enter your email",
                    leadingIcon = R.drawable.email,
                    keyboardType = KeyboardType.Email
                )
                SamYogaTextBox(
                    value = uiState.value.rePassword,
                    onValueChange = viewModel::setRePassword,
                    placeholder = "Enter your password",
                    leadingIcon = R.drawable.password,
                    keyboardType = KeyboardType.Password
                )
                SamYogaTextBox(
                    value = uiState.value.password,
                    onValueChange = viewModel::setPassword,
                    placeholder = "Re-enter your password",
                    leadingIcon = R.drawable.password,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (uiState.value.email.isEmpty() || uiState.value.password.isEmpty() ||
                                uiState.value.rePassword.isEmpty() || uiState.value.name.isEmpty()
                            ) {
                                Toast.makeText(
                                    context,
                                    "Please fill all the fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@KeyboardActions
                            }
                            if (uiState.value.password != uiState.value.rePassword) {
                                Toast.makeText(
                                    context,
                                    "Passwords do not match",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@KeyboardActions
                            }

                            viewModel.signUp(
                                name = viewModel.uiState.value.name,
                                email = viewModel.uiState.value.email,
                                password = viewModel.uiState.value.password,
                                onSuccess = {
                                    navController.navigate(Route.Main) {
                                        popUpTo(Route.Login) { inclusive = true }
                                    }
                                },
                                onError = { error ->
                                    scope.launch(Dispatchers.Main) {
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    )
                )
                SamYogaButton(
                    text = "Sign up",
                    onClick = {
                        if (uiState.value.email.isEmpty() || uiState.value.password.isEmpty() ||
                            uiState.value.rePassword.isEmpty() || uiState.value.name.isEmpty()
                        ) {
                            Toast.makeText(
                                context,
                                "Please fill all the fields",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@SamYogaButton
                        }
                        if (uiState.value.password != uiState.value.rePassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT)
                                .show()
                            return@SamYogaButton
                        }

                        viewModel.signUp(
                            name = viewModel.uiState.value.name,
                            email = viewModel.uiState.value.email,
                            password = viewModel.uiState.value.password,
                            onSuccess = {
                                navController.navigate(Route.Main) {
                                    popUpTo(Route.Login) { inclusive = true }
                                }
                            },
                            onError = { error ->
                                scope.launch(Dispatchers.Main) {
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    },
                    color = Color.White,
                    textColor = OffBlack,
                    enabled = !uiState.value.loading,
                    loading = uiState.value.loading,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        innerPadding = PaddingValues(),
        viewModel = AuthViewModel(),
        navController = NavController(LocalContext.current)
    )
}