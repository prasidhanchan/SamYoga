package com.sam.yoga.presentation.components

import android.content.ClipData
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.R
import com.sam.yoga.domain.Util.boldRegex
import com.sam.yoga.domain.models.Chat
import com.sam.yoga.formatChatTimestamp
import com.sam.yoga.presentation.theme.BrandColor
import com.sam.yoga.presentation.theme.DarkBlue
import com.sam.yoga.presentation.theme.LightBlue
import com.sam.yoga.presentation.theme.OffBlack
import com.sam.yoga.presentation.theme.RedColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    chat: Chat,
    onSaveClick: () -> Unit,
    isGenerating: Boolean = false,
    state: ScrollState
) {
    val isUserMessage = chat.sender == "USER"

    val userBubbleColor = OffBlack
    val aiBubbleColor = BrandColor.copy(alpha = 0.9f)
    val backgroundColor = when (chat.sender) {
        "USER" -> userBubbleColor
        "AI" -> aiBubbleColor
        else -> RedColor
    }

    var isCopied by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = chat.message, key2 = isGenerating) {
        if (isGenerating) {
            state.scrollTo(state.maxValue)
            delay(1000L)
        }
    }

    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (!isUserMessage) {
            TypingIndicatorBlob(isActive = isGenerating)
        }

        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start
        ) {
            Surface(
                modifier = Modifier.widthIn(max = 280.dp),
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = if (isUserMessage) 12.dp else 0.dp,
                    topEnd = if (isUserMessage) 0.dp else 12.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            ) {
                StyledText(
                    text = chat.message,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.widthIn(max = 280.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = chat.timeStamp.formatChatTimestamp(),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                if (!isUserMessage) {
                    Icon(
                        painter = painterResource(id = if (isCopied) R.drawable.done else R.drawable.copy),
                        contentDescription = "Copy",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    scope.launch {
                                        clipboard.setClipEntry(
                                            ClipEntry(
                                                ClipData.newPlainText(
                                                    chat.message,
                                                    chat.message
                                                )
                                            )
                                        )
                                        isCopied = true
                                        delay(2000L)
                                        isCopied = false
                                    }
                                },
                                indication = null,
                                interactionSource = remember(::MutableInteractionSource),
                            )
                            .padding(start = 10.dp)
                    )
                    Icon(
                        painter = painterResource(id = if (isSaved) R.drawable.done else R.drawable.saved_outlined),
                        contentDescription = "Copy",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    scope.launch {
                                        onSaveClick()
                                        isSaved = true
                                        delay(2000L)
                                        isSaved = false
                                    }
                                },
                                indication = null,
                                interactionSource = remember(::MutableInteractionSource),
                            )
                            .padding(start = 15.dp)
                    )
                }
            }
        }
    }

}

@Composable
private fun TypingIndicatorBlob(
    modifier: Modifier = Modifier,
    isActive: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnim"
    )

    Box(
        modifier = modifier
            .size(20.dp)
            .graphicsLayer {
                scaleX = if (isActive) scale else 1f
                scaleY = if (isActive) scale else 1f
            }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(LightBlue, DarkBlue), // Light to dark blue
                    start = Offset.Zero,
                    end = Offset.Infinite
                ),
                shape = CircleShape
            )
    )
}

@Composable
fun StyledText(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        var lastIndex = 0
        for (match in boldRegex.findAll(text)) {
            val start = match.range.first
            val end = match.range.last + 1
            val boldText = match.groupValues[1]

            // Add text before bold part
            append(text.substring(lastIndex, start))

            // Add bold part
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                append(boldText)
            }

            lastIndex = end
        }
        // Add remaining text after last match
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }

    Text(
        text = annotatedText,
        style = style,
        color = color,
        fontSize = fontSize,
        textAlign = textAlign,
        modifier = modifier
    )

}

@Preview
@Composable
private fun ChatBubblePreview() {
    ChatBubble(
        chat = Chat(
            message = "Hello how are you?",
            sender = "AI",
            timeStamp = 100089743L,
            isGenerating = true
        ),
        onSaveClick = { },
        isGenerating = true,
        state = ScrollState(0)
    )
}