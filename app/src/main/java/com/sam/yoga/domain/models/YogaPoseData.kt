package com.sam.yoga.domain.models

import androidx.compose.runtime.Stable

@Stable
data class YogaPoseData(
    var userId: String = "",
    val name: String = "",
    val description: String = "",
    val level: String = "",
    val time: Long = 0L,
    val image: String = "",
    val timeStamp: Long = System.currentTimeMillis()
)