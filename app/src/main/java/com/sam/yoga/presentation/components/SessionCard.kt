package com.sam.yoga.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.R
import com.sam.yoga.presentation.theme.OffBlack

@Composable
fun SessionCard(
    poseLevel: String,
    @DrawableRes image: Int,
    time: Long,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        color = OffBlack,
        onClick = onStartClick
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = poseLevel,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp)
            )
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "$poseLevel Pose",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "$time min",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.play),
                contentDescription = "Play",
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun SessionCardPreview() {
    SessionCard(
        poseLevel = "Beginner",
        image = R.drawable.ardha_chandrasana,
        time = 2L,
        onStartClick = { }
    )
}