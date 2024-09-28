package com.example.doc_di.domain.chatbot

import com.example.doc_di.domain.chatbot.dto.ChatBotClientDto
import com.example.doc_di.domain.chatbot.dto.RasaDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ChatBotApi {
    @POST("chatbot")
    suspend fun chatWithRasa(
        @Body chatBotClientDto: ChatBotClientDto,
    ): Response<SuccessResponse<List<RasaDto>>>
}