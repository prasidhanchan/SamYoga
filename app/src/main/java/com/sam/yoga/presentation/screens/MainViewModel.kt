package com.sam.yoga.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.mlkit.vision.pose.Pose
import com.sam.yoga.domain.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel : ViewModel() {

    var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
        private set

    val currentUser = FirebaseAuth.getInstance().currentUser
    val firebaseAuth = FirebaseAuth.getInstance()
    val fireStore = FirebaseFirestore.getInstance()

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

            uiState.update { it.copy(loading = false) }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signOut()
            onSuccess()
            uiState.update { it.copy(user = null) }
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