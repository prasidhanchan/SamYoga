package com.sam.yoga.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.mlkit.vision.pose.Pose
import com.sam.yoga.R
import com.sam.yoga.data.ImageAnalyzer
import com.sam.yoga.data.YogaPoseClassifierImpl
import com.sam.yoga.domain.Util.getCorrectionTips
import com.sam.yoga.domain.models.Classification
import com.sam.yoga.presentation.components.CameraPreview

@Composable
fun ScanScreen(
    poseName: String,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    var classifications by remember { mutableStateOf(emptyList<Classification>()) }
    var detectedPoseName by remember { mutableStateOf("") }
    var detectedPose by remember { mutableStateOf<Pose?>(null) }
    var imageWidth by remember { mutableIntStateOf(0) }
    var imageHeight by remember { mutableIntStateOf(0) }
    var correctionTips by remember { mutableStateOf<List<String>>(emptyList()) }
    val analyzer = remember {
        ImageAnalyzer(
            classifier = YogaPoseClassifierImpl(context = context),
            onResult = { imageClassifications ->
                classifications = imageClassifications
                detectedPoseName = imageClassifications.firstOrNull()?.name ?: context.getString(R.string.pose_not_found)
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
        if (detectedPoseName != poseName) {
            correctionTips = getCorrectionTips(poseName)
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF232222)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = poseName,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    if (detectedPoseName == poseName) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.great_posture),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = correctionTips.joinToString("\n\n"),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}