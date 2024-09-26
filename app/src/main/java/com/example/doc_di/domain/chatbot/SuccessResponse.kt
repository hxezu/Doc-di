package com.example.doc_di.domain.chatbot

data class SuccessResponse<T>(
    val data: T,
    val status: Int
)