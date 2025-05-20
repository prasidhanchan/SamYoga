package com.sam.yoga.domain.models

data class YogaPose(
    val name: String,
    val description: String,
    val level: String,
    val time: Long,
    val image: String
)