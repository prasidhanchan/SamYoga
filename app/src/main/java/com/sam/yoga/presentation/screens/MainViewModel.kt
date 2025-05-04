package com.sam.yoga.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.mlkit.vision.pose.Pose
import com.sam.yoga.BuildConfig.apiKey
import com.sam.yoga.domain.Util.systemPrompt
import com.sam.yoga.domain.mappers.toChat
import com.sam.yoga.domain.mappers.toChatData
import com.sam.yoga.domain.mappers.toYogaPose
import com.sam.yoga.domain.models.Chat
import com.sam.yoga.domain.models.ChatData
import com.sam.yoga.domain.models.User
import com.sam.yoga.domain.models.YogaPoseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
        private set

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firebaseAuth = FirebaseAuth.getInstance()
    val fireStore = FirebaseFirestore.getInstance()

    private val config = generationConfig {
        temperature = 0.8f
        topK = 40
        topP = 0.9f
        maxOutputTokens = 1000
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash-preview-04-17",
        apiKey = apiKey,
        generationConfig = config
    )

    private val previousQuestionsAsked = mutableListOf<String>()

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            if (currentUser != null) {
                val userId = firebaseAuth.currentUser?.uid!!
                fireStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { docSnap ->
                        val user = docSnap.toObject<User>()
                        uiState.update { it.copy(user = user) }
                    }
                    .await()
            }
        }

        uiState.update { it.copy(loading = false) }
    }

    fun updateUserProfile(
        name: String,
        gender: String,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = firebaseAuth.currentUser?.uid!!
            fireStore.collection("Users")
                .document(userId)
                .update(
                    mapOf(
                        "name" to name,
                        "gender" to gender
                    )
                )
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { error ->
                    onError(error.localizedMessage)
                }
                .await()
        }
    }

    fun getUserActivities() {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            if (currentUser != null) {
                val userId = firebaseAuth.currentUser?.uid!!
                fireStore.collection("Activities")
                    .whereEqualTo("userId", userId)
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { docSnap ->
                        val activities = docSnap.toObjects<YogaPoseData>()
                        val yogaPoses = activities.map { it.toYogaPose() }
                        viewModelScope.launch(Dispatchers.Main) {
                            uiState.update { it.copy(activities = yogaPoses) }
                        }
                    }
                    .await()
            }

            viewModelScope.launch(Dispatchers.Main) {
                delay(1000L)
                uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun updateUserActivity(
        yogaPose: YogaPoseData,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = firebaseAuth.currentUser?.uid!!

            fireStore.collection("Activities")
                .document(yogaPose.name + id)
                .set(yogaPose.apply { userId = id })
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { error ->
                    onError(error.localizedMessage)
                }
                .await()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signOut()
            onSuccess()
            uiState.update { it.copy(user = null) }
        }
    }

    fun sendPrompt(
        prompt: String,
        userChat: Chat
    ) {
        uiState.value.chatHistory.add(userChat) // Set user chat
        previousQuestionsAsked.add(prompt)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fullPrompt = "$systemPrompt\n\n" +
                        "These are the previous questions asked with you so that you can remember the chat: " +
                        "${previousQuestionsAsked.joinToString(",")}\n\n" +
                        "The user's name is ${uiState.value.user?.name}\n\n" +
                        "User: $prompt"
                val response = generativeModel.generateContent(
                    content {
                        text(fullPrompt)
                    }
                )

                val aiChat = Chat(
                    message = "",
                    sender = "AI",
                    timeStamp = System.currentTimeMillis(),
                    isGenerating = true
                )

                val fullResponse = response.text
                    ?.substringAfterLast("#")
                    ?.trim()
                    .orEmpty()

                // Add empty message first
                withContext(Dispatchers.Main) {
                    uiState.value.chatHistory.add(aiChat)
                }

                // Typing animation - character by character
                for (i in fullResponse.indices) {
                    delay(20) // typing speed
                    val currentMessage = fullResponse.substring(0, i + 1)

                    withContext(Dispatchers.Main) {
                        uiState.value.chatHistory[uiState.value.chatHistory.lastIndex] =
                            aiChat.copy(message = currentMessage)
                    }
                }

                uiState.value.chatHistory[uiState.value.chatHistory.lastIndex].isGenerating = false
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.localizedMessage?.let { error ->
                        val errorChat = Chat(
                            message = error,
                            sender = "ERROR",
                            timeStamp = System.currentTimeMillis(),
                            isGenerating = false
                        )
                        uiState.value.chatHistory.add(errorChat)
                        uiState.value.chatHistory[uiState.value.chatHistory.lastIndex].isGenerating =
                            false
                    }
                }
            }
        }
    }

    fun getSaved() {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            if (currentUser != null) {
                val userId = firebaseAuth.currentUser?.uid!!
                fireStore.collection("Saved")
                    .whereEqualTo("userId", userId)
                    .orderBy("timeStamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { docSnap ->
                        val saved = docSnap.toObjects<ChatData>()
                        uiState.update { it.copy(saved = saved.map { it.toChat() }) }
                    }
                    .await()
            }

            viewModelScope.launch(Dispatchers.Main) {
                delay(1000L)
                uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun saveChat(chat: Chat) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatData = chat.toChatData()
            val id = firebaseAuth.currentUser?.uid!!

            chatData.apply { userId = id }
            fireStore.collection("Saved")
                .document(chat.message.subSequence(0, 10).toString() + id + chat.timeStamp)
                .set(chatData)
                .await()
        }
    }

    fun deleteSaved(chat: Chat) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatData = chat.toChatData()
            val id = firebaseAuth.currentUser?.uid!!

            chatData.apply { userId = id }
            fireStore.collection("Saved")
                .document(chat.message.subSequence(0, 10).toString() + id + chat.timeStamp)
                .delete()
                .addOnSuccessListener { getSaved() }
                .await()
        }
    }

    fun clearScanState() {
        uiState.update {
            it.copy(
                countDown = 10,
                poseCount = 0,
                isPlaying = true,
                detectedPose = null,
                detectedPoseName = null,
                forcePause = false,
                loading = false
            )
        }
    }

    fun setCountDown(countDown: Int) {
        uiState.update { it.copy(countDown = countDown) }
    }

    fun setPoseCount(poseCount: Int) {
        uiState.update { it.copy(poseCount = poseCount) }
    }

    fun setIsPlaying(isPlaying: Boolean) {
        uiState.update { it.copy(isPlaying = isPlaying) }
    }

    fun setDetectedPose(detectedPose: Pose?) {
        uiState.update { it.copy(detectedPose = detectedPose) }
    }

    fun setDetectedPoseName(detectedPoseName: String?) {
        uiState.update { it.copy(detectedPoseName = detectedPoseName) }
    }

    fun setForcePause(forcePause: Boolean) {
        uiState.update { it.copy(forcePause = forcePause) }
    }
}