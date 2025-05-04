package com.sam.yoga.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sam.yoga.R
import com.sam.yoga.domain.models.Chat
import com.sam.yoga.getGreetings
import com.sam.yoga.presentation.components.AppBar
import com.sam.yoga.presentation.components.ChatBubble
import com.sam.yoga.presentation.components.ChatTextBox
import com.sam.yoga.presentation.components.SendButton
import com.sam.yoga.presentation.theme.SamYogaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel
) {
    var query by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val localKeyboard = LocalSoftwareKeyboardController.current
    val state = rememberScrollState()
    val scope = rememberCoroutineScope()

    val isGenerationActive = uiState.chatHistory.lastOrNull()?.isGenerating

    if (uiState.chatHistory.isNotEmpty()) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppBar(title = "Explore")
            Column(
                modifier = Modifier
                    .padding(bottom = 60.dp)
                    .fillMaxSize()
                    .verticalScroll(state),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.chatHistory.forEach { chat ->
                    ChatBubble(
                        chat = chat,
                        onSaveClick = { viewModel.saveChat(chat) },
                        isGenerating = chat.isGenerating,
                        state = state
                    )
                }
            }
        }
    } else {
        NewChatView(
            userName = uiState.user?.name ?: "User",
            modifier = Modifier.padding(innerPadding)
        )
    }

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChatTextBox(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(15.dp))
                SendButton(
                    onClick = {
                        scope.launch {
                            if (query.isNotEmpty()) {
                                viewModel.sendPrompt(
                                    prompt = query,
                                    userChat =
                                        Chat(
                                            message = query,
                                            sender = "USER",
                                            timeStamp = System.currentTimeMillis(),
                                            isGenerating = false // Not required for USER
                                        )
                                )
                                query = ""
                                localKeyboard?.hide()
                                delay(500L)
                                state.animateScrollTo(state.maxValue)
                            }
                        }
                    },
                    enable = if (isGenerationActive != null)
                        !isGenerationActive
                    else true
                )
            }
        }
    }
}

@Composable
private fun NewChatView(
    userName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AppBar(title = "Explore")

        Column(
            modifier = modifier
                .padding(bottom = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "${getGreetings()}, $userName.",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "How can I help you today?",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreScreenPreview() {
    SamYogaTheme {
        ExploreScreen(
            innerPadding = PaddingValues(),
            viewModel = MainViewModel()
        )
    }
}