package com.example.doc_di.domain.chatbot

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class ChatBotImpl(private val chatBotApi: ChatBotApi) {
    suspend fun chatWithRasa(chatBotClientDto: ChatBotClientDto): Response<SuccessResponse<List<RasaDto>>> {
        return chatBotApi.chatWithRasa(chatBotClientDto)
    }

}