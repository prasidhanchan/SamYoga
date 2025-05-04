package com.sam.yoga.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sam.yoga.R

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    onClick: () -> Unit,
    color: Color = Color.White,
) {
    Surface(
        modifier = modifier.size(50.dp),
        onClick = onClick,
        enabled = enable,
        color = color,
        shape = CircleShape,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.send),
            contentDescription = "Send",
            tint = Color.Unspecified,
            modifier = Modifier.scale(0.5f)
        )
    }
}