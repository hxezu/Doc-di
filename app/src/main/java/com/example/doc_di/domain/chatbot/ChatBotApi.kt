package com.example.doc_di.domain.chatbot

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET

interface ChatBotApi {
    @POST("chatbot")
    suspend fun chatWithRasa(
        @Body chatBotClientDto: ChatBotClientDto,
    ): Response<SuccessResponse<List<RasaDto>>>
}