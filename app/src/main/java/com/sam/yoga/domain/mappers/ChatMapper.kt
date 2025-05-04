package com.sam.yoga.domain.mappers

import com.sam.yoga.domain.models.Chat
import com.sam.yoga.domain.models.ChatData

fun ChatData.toChat(): Chat {
    return Chat(
        message = this.message,
        sender = this.sender,
        timeStamp = this.timeStamp,
        isGenerating = false
    )
}

fun Chat.toChatData(): ChatData {
    return ChatData(
        message = this.message,
        sender = this.sender,
        timeStamp = this.timeStamp
    )
}