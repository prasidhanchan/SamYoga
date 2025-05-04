package com.sam.yoga.domain.models

data class Chat(
    val message: String,
    val sender: String,
    val timeStamp: Long,
    var isGenerating: Boolean
)