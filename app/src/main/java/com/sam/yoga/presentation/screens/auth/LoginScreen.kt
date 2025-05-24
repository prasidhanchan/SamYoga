package com.sam.yoga.presentation.screens.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
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
fun LoginScreen(
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
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painterResource(id = R.drawable.login_image),
                contentDescription = "Login"
            )
            Text(
                text = "Login",
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
                value = uiState.value.email,
                onValueChange = viewModel::setEmail,
                placeholder = "Enter your email",
                leadingIcon = R.drawable.email,
                keyboardType = KeyboardType.Email
            )
            SamYogaTextBox(
                value = uiState.value.password,
                onValueChange = viewModel::setPassword,
                placeholder = "Enter your password",
                leadingIcon = R.drawable.password,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (uiState.value.email.isEmpty() || uiState.value.password.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please enter email and password",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@KeyboardActions
                        }

                        viewModel.login(
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
            Spacer(modifier = Modifier.height(20.dp))
            SamYogaButton(
                text = "Login",
                onClick = {
                    if (uiState.value.email.isEmpty() || uiState.value.password.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Please enter email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@SamYogaButton
                    }

                    viewModel.login(
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
                loading = uiState.value.loading
            )
        }
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 20.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White
                        )
                    ) {
                        append("Don't have an account?")
                    }

                    withLink(
                        link = LinkAnnotation.Clickable(
                            tag = "SignUp",
                            styles = TextLinkStyles(
                                style = SpanStyle(color = OffBlack)
                            ),
                            linkInteractionListener = LinkInteractionListener {
                                navController.navigate(Route.SignUp)
                            }
                        )
                    ) {
                        append(" ")
                        append("SignUp")
                    }
                },
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        innerPadding = PaddingValues(),
        viewModel = AuthViewModel(),
        navController = NavController(LocalContext.current)
    )
}