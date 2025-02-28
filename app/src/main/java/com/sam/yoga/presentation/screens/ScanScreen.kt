package com.sam.yoga.presentation.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sam.yoga.data.YogaPoseClassifierImpl
import com.sam.yoga.domain.models.Classification
import com.sam.yoga.presentation.components.CameraPreview
import com.sam.yoga.presentation.components.SamYogaButton
import com.sam.yoga.R
import com.sam.yoga.UiState
import com.sam.yoga.data.ImageAnalyzer

@Composable
fun ScanScreen(
    viewModel: MainViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var classifications by remember { mutableStateOf(emptyList<Classification>()) }
    var detectedImage by remember { mutableStateOf("") }
    val analyzer = remember {
        ImageAnalyzer(
            classifier = YogaPoseClassifierImpl(context = context),
            onResult = { imageClassifications ->
                classifications = imageClassifications
                detectedImage = imageClassifications.firstOrNull()?.name ?: "Pose not found!"
                Log.d("IMAGGEE", "ScanScreen: $detectedImage")
            }
        )
    }
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                analyzer
            )
        }
    }

//    LaunchedEffect(classifications) {
//        if (classifications.isNotEmpty() && detectedImage.isEmpty()) {
//            detectedImage = classifications.first().name
//            Log.d("IMAGGEE", "ScanScreen: $detectedImage")
////            viewModel.sendPrompt(context.getString(R.string.prompt_placeholder, detectedImage))
//        }
//    }

    var cardHeight by remember { mutableStateOf(70.dp) }
    val animatedCardHeight by animateDpAsState(
        targetValue = cardHeight,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = if (cardHeight == 70.dp)
                Spring.DampingRatioLowBouncy else
                Spring.DampingRatioMediumBouncy
        ),
        label = "animatedHeight"
    )

    val colorList = listOf(
        0xFF2196F3,
        0xFF4CAF50,
        0xFFD5A410,
        0xFFE56A94
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )
        AnimatedVisibility(
            visible = uiState !is UiState.Success,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.scanner),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(animatedCardHeight),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .wrapContentSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (detectedImage.isNotBlank()) {
                        Text(
                            text = detectedImage,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    Text(
                        text = when (uiState) {
                            is UiState.Loading -> {
                                if (detectedImage.isNotBlank()) cardHeight = 200.dp
                                stringResource(R.string.loading)
                            }

                            is UiState.Success -> {
                                cardHeight = 480.dp
                                (uiState as UiState.Success).outputText
                                    .substringBeforeLast("[")
                                    .trim()
                            }

                            is UiState.Error, UiState.Initial -> {
                                if (detectedImage.isBlank()) cardHeight = 70.dp
                                stringResource(R.string.analyzing)
                            }
                        },
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        textAlign = TextAlign.Center
                    )
                    ScannerBottomComp(
                        detectedImage = detectedImage,
                        onRetryClick = {
                            detectedImage = ""
                            viewModel.clearUiState()
                        }
                    )
                }
            }
        }
    }

    BackHandler {
        navHostController.popBackStack()
        viewModel.clearUiState()
    }
}

@Composable
private fun ScannerBottomComp(
    detectedImage: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (detectedImage.isNotBlank()) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SamYogaButton(
                text = stringResource(R.string.retry),
                onClick = onRetryClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}