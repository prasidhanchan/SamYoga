package com.sam.yoga.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.R

@Composable
fun ScanSuggestionCard(
    poseName: String,
    poseLevel: String?,
    detectedPoseName: String,
    correctionTips: List<String>,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    enableNext: Boolean,
    enablePrevious: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (poseLevel != null) {
                    Icon(
                        painterResource(id = R.drawable.prev),
                        contentDescription = "Previous",
                        tint = if (enablePrevious) Color.White else Color.Gray,
                        modifier = Modifier.clickable(
                            onClick = onPrevious,
                            enabled = enablePrevious,
                            indication = null,
                            interactionSource = remember(::MutableInteractionSource)
                        )
                    )
                }
                Button(
                    onClick = onToggle,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        painterResource(id = if (isPlaying) R.drawable.pause_button else R.drawable.play_button),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.Black
                    )
                }
                if (poseLevel != null) {
                    Icon(
                        painterResource(id = R.drawable.next),
                        contentDescription = "Previous",
                        tint = if (enableNext) Color.White else Color.Gray,
                        modifier = Modifier.clickable(
                            onClick = onNext,
                            enabled = enableNext,
                            indication = null,
                            interactionSource = remember(::MutableInteractionSource)
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScanSuggestionCardPreview() {
    ScanSuggestionCard(
        poseName = "Tree Pose II",
        poseLevel = "Beginner",
        detectedPoseName = "Tree Pose",
        correctionTips = listOf("Tip 1", "Tip 2", "Tip 3"),
        isPlaying = false,
        onToggle = { },
        onNext = { },
        onPrevious = { },
        enableNext = true,
        enablePrevious = true
    )
}