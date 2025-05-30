package com.sam.yoga.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sam.yoga.BuildConfig.storageUrl
import com.sam.yoga.R
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.PurpleColor
import com.sam.yoga.presentation.theme.RedColor
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun PoseCard(
    poseName: String,
    level: String,
    time: Long,
    image: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    color: Color = when (level) {
        "Beginner" -> BrandColor
        "Intermediate" -> PurpleColor
        else -> RedColor
    }
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .width(170.dp)
            .height(280.dp),
        shape = RoundedCornerShape(20.dp),
        color = color,
        onClick = { onClick(poseName) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = poseName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color.White,
                textAlign = TextAlign.Start
            )
            Text(
                text = level,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White,
                textAlign = TextAlign.Start
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$time min",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play",
                    tint = Color.White
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = poseName
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ActionIconPreview() {
    SamYogaTheme {
        PoseCard(
            poseName = "Chair Pose",
            level = "Beginner",
            time = 2L,
            image = "$storageUrl/ardha_chandrasana.png",
            onClick = { }
        )
    }
}