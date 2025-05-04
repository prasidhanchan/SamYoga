package com.sam.yoga.presentation.screens

import androidx.compose.runtime.mutableStateListOf
import com.google.mlkit.vision.pose.Pose
import com.sam.yoga.domain.models.Chat
import com.sam.yoga.domain.models.User
import com.sam.yoga.domain.models.YogaPose

data class UiState(
    var countDown: Int = 10,
    var poseCount: Int = 0,
    var isPlaying: Boolean = true,
    var detectedPose: Pose? = null,
    var detectedPoseName: String? = null,
    var forcePause: Boolean = false,
    var loading: Boolean = false,
    var user: User? = null,
    var activities: List<YogaPose> = emptyList(),
    var saved: List<Chat> = emptyList(),
    var chatHistory: MutableList<Chat> = mutableStateListOf()
)