package com.example.doc_di.domain.model

data class Chat(
    val id: Int,
    val email: String,
    val messages: MutableList<Message> // List of messages
)