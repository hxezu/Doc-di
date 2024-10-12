package com.example.doc_di.domain.model

data class Message(
    val id: Int = 0,
    val content: String = "",
    val time: String = "",
    val user: Boolean = false // Flag to check if the message is from the user
)
