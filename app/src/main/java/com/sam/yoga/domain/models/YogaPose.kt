package com.sam.yoga.domain.models

data class YogaPose(
    val name: String,
    val description: String,
    val color: String,
    val timeStamp: Long = System.currentTimeMillis()
)