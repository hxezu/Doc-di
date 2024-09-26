package com.example.doc_di.domain.chatbot

data class SuccessResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)