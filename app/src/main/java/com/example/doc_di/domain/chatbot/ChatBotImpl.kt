package com.example.doc_di.domain.chatbot

import com.example.doc_di.domain.ServerResponse
import com.example.doc_di.domain.chatbot.dto.ChatBotClientDto
import com.example.doc_di.domain.chatbot.dto.RasaDto
import retrofit2.Response

class ChatBotImpl(private val chatBotApi: ChatBotApi) {
    suspend fun chatWithRasa(chatBotClientDto: ChatBotClientDto): Response<ServerResponse<List<RasaDto>>> {
        return chatBotApi.chatWithRasa(chatBotClientDto)
    }
}