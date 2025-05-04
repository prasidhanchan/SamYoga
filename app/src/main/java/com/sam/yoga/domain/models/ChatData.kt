package com.sam.yoga.domain.models

data class ChatData(
    var userId: String = "",
    var message: String = "",
    var sender: String = "",
    var timeStamp: Long = 0L
)