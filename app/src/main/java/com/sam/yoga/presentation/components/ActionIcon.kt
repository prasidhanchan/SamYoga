package com.sam.yoga.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.R
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun ActionIcon(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = BrandColor
) {
    Surface(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(15.dp),
        color = color,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
                .height(160.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ActionIconPreview() {
    SamYogaTheme {
        ActionIcon(
            text = "Scan now",
            icon = painterResource(id = R.drawable.scan),
            onClick = { }
        )
    }
}