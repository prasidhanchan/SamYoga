package com.sam.yoga.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sam.yoga.R
import com.sam.yoga.presentation.theme.OffBlack

@Composable
fun ProfileImage(
    gender: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .size(100.dp),
        shape = CircleShape,
        color = OffBlack
    ) {
        Image(
            painter = painterResource(
                id = if (gender == "Female")
                    R.drawable.avatar_female else
                    R.drawable.avatar_male
            ),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(1.22f)
        )
    }
}