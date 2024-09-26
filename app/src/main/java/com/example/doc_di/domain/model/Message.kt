package com.example.doc_di.domain.model

data class Message(
    val id: Int,
    val content: String, // The content of the message
    val time: String, // The time when the message was sent
    val isUser: Boolean // Flag to check if the message is from the user
)
