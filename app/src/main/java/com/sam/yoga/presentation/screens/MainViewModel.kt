package com.sam.yoga.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.pose.Pose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
        private set

    val firebaseAuth = FirebaseAuth.getInstance()

    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (error: String?) -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    onSuccess()
                    uiState.update { it.copy(loading = false) }
                }
                .addOnFailureListener { error ->
                    onError(error.localizedMessage)
                    uiState.update { it.copy(loading = false) }
                }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (error: String?) -> Unit
    ) {
        uiState.update { it.copy(loading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    onSuccess()
                    uiState.update { it.copy(loading = false) }
                }
                .addOnFailureListener { error ->
                    onError(error.localizedMessage)
                    uiState.update { it.copy(loading = false) }
                }
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