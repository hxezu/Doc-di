package com.example.doc_di.domain.chatbot.dto


data class GeminiDto(
    val sender : String?,
    val data : List<GeminiSenderDataDto>?,
)
