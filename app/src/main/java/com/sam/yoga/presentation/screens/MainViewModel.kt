package com.sam.yoga.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.sam.yoga.BuildConfig
import com.sam.yoga.UiState
import com.sam.yoga.domain.models.YogaPose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
        private set

    var poses: MutableStateFlow<List<YogaPose>> = MutableStateFlow(emptyList())
        private set

    private val config = generationConfig {
        temperature = 0.8f
        topK = 40
        topP = 0.9f
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey,
        generationConfig = config
    )

    fun sendPrompt(prompt: String) {
        uiState.update { UiState.Loading }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text
                    ?.substringAfterLast("#")
                    ?.trim()
                    ?.let { outputContent ->
                        uiState.update { UiState.Success(outputContent) }
                    }
            } catch (e: Exception) {
                uiState.update { UiState.Error(e.localizedMessage ?: "") }
            }
        }
    }

    fun clearUiState() = uiState.update { UiState.Initial }
}