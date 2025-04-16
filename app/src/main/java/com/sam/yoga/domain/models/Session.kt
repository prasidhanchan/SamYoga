package com.sam.yoga.domain.models

data class Session(
    val level: String,
    val poses: List<YogaPose>,
    val image: Int,
    val time: Long
)