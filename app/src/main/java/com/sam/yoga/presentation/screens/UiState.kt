package com.sam.yoga.presentation.screens

import com.google.mlkit.vision.pose.Pose

data class UiState(
    var countDown: Int = 10,
    var poseCount: Int = 0,
    var isPlaying: Boolean = true,
    var detectedPose: Pose? = null,
    var detectedPoseName: String? = null,
    var forcePause: Boolean = false,
    var loading: Boolean = false
)
