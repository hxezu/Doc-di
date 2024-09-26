package com.example.doc_di.domain.chatbot

import retrofit2.Response

class ChatBotImpl(private val chatBotApi: ChatBotApi) {

    suspend fun chatWithRasa(chatBotClientDto: ChatBotClientDto): Response<SuccessResponse<String>> {
        return chatBotApi.chatWithRasa(chatBotClientDto)
    }

}