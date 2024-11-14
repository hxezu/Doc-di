package com.example.doc_di.domain.chatbot

import com.example.doc_di.domain.ServerResponse
import com.example.doc_di.domain.chatbot.dto.ChatBotClientDto
import com.example.doc_di.domain.chatbot.dto.RasaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatBotApi {
    @POST("chatbot")
    suspend fun chatWithRasa(
        @Body chatBotClientDto: ChatBotClientDto,
    ): Response<ServerResponse<List<RasaDto>>>
}