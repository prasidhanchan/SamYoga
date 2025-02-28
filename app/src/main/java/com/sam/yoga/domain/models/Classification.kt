package com.sam.yoga.domain.models

import androidx.compose.runtime.Stable

@Stable
data class Classification(
    val name: String,
    val score: Float,
    val index: Int
)