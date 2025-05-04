package com.sam.yoga.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.sam.yoga.R
import com.sam.yoga.domain.models.Chat
import com.sam.yoga.formatChatTimestamp
import com.sam.yoga.presentation.theme.OffBlack
import com.sam.yoga.presentation.theme.SamYogaTheme

@Composable
fun SavedCard(
    chat: Chat,
    modifier: Modifier = Modifier,
    color: Color = OffBlack,
    onDeleteClick: (Chat) -> Unit
) {

    Surface(
        modifier = modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        color = color
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            StyledText(
                text = chat.message,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White,
                textAlign = TextAlign.Start
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete Saved",
                    tint = Color.Unspecified,
                    modifier = Modifier.clickable(
                        onClick = { onDeleteClick(chat) },
                        indication = null,
                        interactionSource = remember(::MutableInteractionSource)
                    )
                )
                Text(
                    text = chat.timeStamp.formatChatTimestamp(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SavedCardPreview() {
    SamYogaTheme {
        SavedCard(
            chat = Chat(
                message = "Hello how are you?",
                sender = "AI",
                timeStamp = 100089743L,
                isGenerating = true
            ),
            onDeleteClick = { }
        )
    }
}