package com.sam.yoga.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.R

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    isBackVisible: Boolean = false,
    onBackClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isBackVisible)
            Arrangement.SpaceBetween else
            Arrangement.SpaceEvenly
    ) {
        if (isBackVisible) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier.clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember(::MutableInteractionSource)
                )
            )
        }
        Text(
            text = title,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
        )
        if (isBackVisible) Spacer(Modifier.width(24.dp))
    }
}

@Preview
@Composable
private fun AppBarPreview() {
    AppBar(
        title = "Saved Landmarks",
        onBackClick = { }
    )
}