package com.sam.yoga.presentation.screens

import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sam.yoga.R
import com.sam.yoga.data.ImageAnalyzer
import com.sam.yoga.data.YogaPoseClassifierImpl
import com.sam.yoga.data.YogaTTS
import com.sam.yoga.domain.Util.getCorrectionTips
import com.sam.yoga.domain.Util.poses
import com.sam.yoga.domain.mappers.toYogaPoseData
import com.sam.yoga.domain.models.Classification
import com.sam.yoga.presentation.components.CameraPreview
import com.sam.yoga.presentation.components.ScanSuggestionCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(
    poseName: String,
    poseLevel: String? = null,
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentPoseName by rememberSaveable { mutableStateOf(poseName) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var classifications by remember { mutableStateOf(emptyList<Classification>()) }
    var imageWidth by remember { mutableIntStateOf(0) }
    var imageHeight by remember { mutableIntStateOf(0) }
    var correctionTips by remember { mutableStateOf<List<String>>(emptyList()) }
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

    val yogaTTS = remember { YogaTTS(context) }
    var mediaPlayer: MediaPlayer? = null

    DisposableEffect(Unit) {
        viewModel.updateUserActivity(
            yogaPose = when (poseLevel) {
                "Beginner" -> beginnerPoses.first { it.name == currentPoseName }
                    .toYogaPoseData()

                "Intermediate" -> intermediatePoses.first { it.name == currentPoseName }
                    .toYogaPoseData()

                "Advanced" -> advancedPoses.first { it.name == currentPoseName }
                    .toYogaPoseData()

                else -> poses.first { it.name == currentPoseName }.toYogaPoseData()
            },
            onSuccess = { },
            onError = { error ->
                scope.launch {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )
        onDispose {
            yogaTTS.shutdown()
        }
    }

    val analyzer = remember {
        ImageAnalyzer(
            classifier = YogaPoseClassifierImpl(context = context),
            onResult = { imageClassifications ->
                classifications = imageClassifications
                viewModel.setDetectedPoseName(
                    imageClassifications.firstOrNull()?.name
                        ?: context.getString(R.string.pose_not_found)
                )
            },
            onPoseChange = { pose, width, height ->
                viewModel.setDetectedPose(pose)
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

    LaunchedEffect(uiState.detectedPoseName) {
        if (currentPoseName == uiState.detectedPoseName) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, R.raw.correct_pose)
            mediaPlayer?.setOnCompletionListener { mp ->
                mp.release()
            }
            mediaPlayer?.start()
        }
    }

    LaunchedEffect(
        key1 = currentPoseName,
        key2 = uiState.detectedPoseName,
        key3 = uiState.forcePause
    ) {
        if (uiState.detectedPoseName != currentPoseName && !uiState.forcePause) {
            delay(1000L)
            correctionTips = getCorrectionTips(currentPoseName)
            yogaTTS.speakYogaInstruction(currentPoseName)
            yogaTTS.speakYogaInstruction(correctionTips.joinToString("."))
        } else {
            yogaTTS.stopSpeaking()
        }
    }

    LaunchedEffect(
        key1 = uiState.countDown,
        key2 = uiState.isPlaying,
        key3 = uiState.detectedPoseName
    ) {
        launch {
            delay(1000L)

            viewModel.setIsPlaying(!uiState.forcePause) // change the playing status

            if (
                uiState.detectedPoseName == currentPoseName && // Only start the countdown if the pose is proper
                uiState.countDown > 0 &&
                uiState.isPlaying
            ) {
                yogaTTS.speakYogaInstruction(uiState.countDown.toString()) // Start CountDown Speech
                delay(1000L) // Delay so that it reads correct count
                viewModel.setCountDown(uiState.countDown - 1)
            }

            if (poseLevel != null && uiState.poseCount >= 0 && enableNext && uiState.countDown == 0) {
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

                viewModel.setPoseCount(uiState.poseCount + 1)
                delay(1000L)
                viewModel.setCountDown(10) // Restart Count
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
            text = "${uiState.countDown}",
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
                detectedPoseName = uiState.detectedPoseName.toString(),
                correctionTips = correctionTips,
                isPlaying = uiState.isPlaying,
                onToggle = {
                    viewModel.setIsPlaying(!uiState.isPlaying)
                    viewModel.setForcePause(!uiState.forcePause)
                },
                onNext = {
                    yogaTTS.stopSpeaking() // Stop speaking on Next since it wont change the speech if the pose is switched
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

                    viewModel.setPoseCount(uiState.poseCount + 1)
                    viewModel.setCountDown(10) // Restart Count
                },
                onPrevious = {
                    yogaTTS.stopSpeaking() // Stop speaking on Prev since it wont change the speech if the pose is switched
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

                    viewModel.setPoseCount(uiState.poseCount - 1)
                    viewModel.setCountDown(10) // Restart Count
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
        viewModel.clearScanState()
    }
}