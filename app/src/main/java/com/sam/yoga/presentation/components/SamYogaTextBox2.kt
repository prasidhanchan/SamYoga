package com.sam.yoga.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sam.yoga.R
import com.sam.yoga.presentation.theme.OffBlack

@Composable
fun SamYogaTextBox2(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIcon: Int? = null,
    placeholder: String = "",
    label: String = "",
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    enable: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        },
        label = {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        },
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = placeholder
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OffBlack.copy(alpha = 0.8f),
            unfocusedContainerColor = OffBlack.copy(alpha = 0.8f),
            disabledContainerColor = OffBlack.copy(alpha = 0.8f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = capitalization
        ),
        keyboardActions = keyboardActions,
        visualTransformation = if (isPassword && !showPassword)
            PasswordVisualTransformation() else
            VisualTransformation.None,
        enabled = enable
    )
}

@Preview
@Composable
private fun SamYogaTextBox2Preview() {
    SamYogaTextBox2(
        value = "Hello",
        onValueChange = { },
        leadingIcon = R.drawable.email
    )
}