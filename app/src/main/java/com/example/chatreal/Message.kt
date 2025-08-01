package com.example.chatreal

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    var isSentByMe: Boolean = false  // Will be set dynamically
)
