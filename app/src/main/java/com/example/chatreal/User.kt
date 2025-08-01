package com.example.chatreal

data class User(
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "" // This will be a Firebase Storage URL
)
