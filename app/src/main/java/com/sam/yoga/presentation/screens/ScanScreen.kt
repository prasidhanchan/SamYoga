package com.sam.yoga.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.mlkit.vision.pose.Pose
import com.sam.yoga.R
import com.sam.yoga.data.ImageAnalyzer
import com.sam.yoga.data.YogaPoseClassifierImpl
import com.sam.yoga.domain.Util.getCorrectionTips
import com.sam.yoga.domain.Util.poses
import com.sam.yoga.domain.models.Classification
import com.sam.yoga.presentation.components.CameraPreview
import com.sam.yoga.presentation.components.ScanSuggestionCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(
    poseName: String,
    poseLevel: String? = null,
    navHostController: NavHostController
) {
    var currentPoseName by remember { mutableStateOf(poseName) }
    var poseCount by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    var classifications by remember { mutableStateOf(emptyList<Classification>()) }
    var detectedPoseName by remember { mutableStateOf("") }
    var detectedPose by remember { mutableStateOf<Pose?>(null) }
    var imageWidth by remember { mutableIntStateOf(0) }
    var imageHeight by remember { mutableIntStateOf(0) }
    var correctionTips by remember { mutableStateOf<List<String>>(emptyList()) }
    var countDown by remember { mutableIntStateOf(10) }
    var isPlaying by remember { mutableStateOf(false) }
    var forcePause by remember { mutableStateOf(false) }
    val beginnerPoses = poses.filter { pose -> pose.level == "Beginner" }
    val intermediatePoses = poses.filter { pose -> pose.level == "Intermediate" }
    val advancedPoses = poses.filter { pose -> pose.level == "Advanced" }
    val enableNext = when (poseLevel) {
        "Beginner" -> beginnerPoses.last().name != currentPoseName
        "Intermediate" -> intermediatePoses.last().name != currentPoseName
        "Advanced" -> advancedPoses.last().name != currentPoseName
        else -> false
    }
    val enablePrev = when (poseLevel) {
        "Beginner" -> beginnerPoses.first().name != currentPoseName
        "Intermediate" -> intermediatePoses.first().name != currentPoseName
        "Advanced" -> advancedPoses.first().name != currentPoseName
        else -> false
    }

    val analyzer = remember {
        ImageAnalyzer(
            classifier = YogaPoseClassifierImpl(context = context),
            onResult = { imageClassifications ->
                classifications = imageClassifications
                detectedPoseName = imageClassifications.firstOrNull()?.name
                    ?: context.getString(R.string.pose_not_found)
            },
            onPoseChange = { pose, width, height ->
                detectedPose = pose
                imageWidth = width
                imageHeight = height
            }
        )
    }
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
//            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                analyzer
            )
        }
    }

    LaunchedEffect(detectedPoseName) {
        if (detectedPoseName != currentPoseName) {
            correctionTips = getCorrectionTips(currentPoseName)
        }
    }

    LaunchedEffect(key1 = countDown, key2 = isPlaying, key3 = detectedPoseName) {
        launch {
            delay(1000L)

            isPlaying =
                detectedPoseName == currentPoseName && !forcePause // change the playing status

            if (
                detectedPoseName == currentPoseName && // Only start countdown if the pose is proper
                countDown > 0 &&
                isPlaying
            ) {
                countDown--
                delay(1000L)
            } else if (poseLevel == null) {
                isPlaying = false
            }

            if (poseLevel != null && poseCount >= 0 && enableNext && countDown == 0) {
                when (poseLevel) {
                    "Beginner" -> {
                        val pose = beginnerPoses.first { it.name == currentPoseName }
                        val poseIndex = beginnerPoses.indexOf(pose)
                        if (poseIndex < beginnerPoses.size - 1) {
                            currentPoseName = beginnerPoses[poseIndex + 1].name
                        } else {
                            beginnerPoses[poseIndex].name
                        }
                    }

                    "Intermediate" -> {
                        val pose = intermediatePoses.first { it.name == currentPoseName }
                        val poseIndex = intermediatePoses.indexOf(pose)
                        if (poseIndex < intermediatePoses.size - 1) {
                            currentPoseName = intermediatePoses[poseIndex + 1].name
                        } else {
                            intermediatePoses[poseIndex].name
                        }
                    }

                    "Advanced" -> {
                        val pose = advancedPoses.first { it.name == currentPoseName }
                        val poseIndex = advancedPoses.indexOf(pose)
                        if (poseIndex < advancedPoses.size - 1) {
                            currentPoseName = advancedPoses[poseIndex + 1].name
                        } else {
                            advancedPoses[poseIndex].name
                        }
                    }
                }

                poseCount++
                delay(1000L)
                countDown = 10 // Restart Count
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "$countDown",
            style = TextStyle(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(bottom = 200.dp)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ScanSuggestionCard(
                poseName = currentPoseName,
                poseLevel = poseLevel,
                detectedPoseName = detectedPoseName,
                correctionTips = correctionTips,
                isPlaying = isPlaying,
                onToggle = {
                    isPlaying = !isPlaying
                    forcePause = !forcePause

                },
                onNext = {
                    when (poseLevel) {
                        "Beginner" -> {
                            val pose = beginnerPoses.first { it.name == currentPoseName }
                            val poseIndex = beginnerPoses.indexOf(pose)
                            if (poseIndex < beginnerPoses.size - 1) {
                                currentPoseName = beginnerPoses[poseIndex + 1].name
                            } else {
                                beginnerPoses[poseIndex].name
                            }
                        }

                        "Intermediate" -> {
                            val pose = intermediatePoses.first { it.name == currentPoseName }
                            val poseIndex = intermediatePoses.indexOf(pose)
                            if (poseIndex < intermediatePoses.size - 1) {
                                currentPoseName = intermediatePoses[poseIndex + 1].name
                            } else {
                                intermediatePoses[poseIndex].name
                            }
                        }

                        "Advanced" -> {
                            val pose = advancedPoses.first { it.name == currentPoseName }
                            val poseIndex = advancedPoses.indexOf(pose)
                            if (poseIndex < advancedPoses.size - 1) {
                                currentPoseName = advancedPoses[poseIndex + 1].name
                            } else {
                                advancedPoses[poseIndex].name
                            }
                        }
                    }

                    poseCount++
                    countDown = 10 // Restart Count
                },
                onPrevious = {
                    when (poseLevel) {
                        "Beginner" -> {
                            val pose = beginnerPoses.first { it.name == currentPoseName }
                            val poseIndex = beginnerPoses.indexOf(pose)
                            if (poseIndex > 0) {
                                currentPoseName = beginnerPoses[poseIndex - 1].name
                            } else {
                                beginnerPoses[poseIndex].name
                            }
                        }

                        "Intermediate" -> {
                            val pose = intermediatePoses.first { it.name == currentPoseName }
                            val poseIndex = intermediatePoses.indexOf(pose)
                            if (poseIndex > 0) {
                                currentPoseName = intermediatePoses[poseIndex - 1].name
                            } else {
                                intermediatePoses[poseIndex].name
                            }
                        }

                        "Advanced" -> {
                            val pose = advancedPoses.first { it.name == currentPoseName }
                            val poseIndex = advancedPoses.indexOf(pose)
                            if (poseIndex > 0) {
                                currentPoseName = advancedPoses[poseIndex - 1].name
                            } else {
                                advancedPoses[poseIndex].name
                            }
                        }
                    }

                    poseCount--
                    countDown = 10 // Restart Count
                },
                enableNext = enableNext,
                enablePrevious = enablePrev,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
            )
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}