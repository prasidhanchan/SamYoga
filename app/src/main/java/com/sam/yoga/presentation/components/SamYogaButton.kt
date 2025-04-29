package com.sam.yoga.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.OffBlack

@Composable
fun SamYogaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = BrandColor,
    textColor: Color = Color.White,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Surface(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled
            )
            .padding(vertical = 5.dp)
            .imePadding()
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(10.dp),
        color = color
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!loading) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            } else {
                CircularProgressIndicator(
                    color = OffBlack,
                    strokeWidth = 3.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}