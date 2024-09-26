package com.example.doc_di.domain.chatbot

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class ChatBotImpl(private val chatBotApi: ChatBotApi) {
    suspend fun chatWithRasa(chatBotClientDto: ChatBotClientDto): Response<SuccessResponse<String>> {
        val apiResponse = chatBotApi.chatWithRasa(chatBotClientDto)

        return if (apiResponse.isSuccessful) {
            Response.success(SuccessResponse<String>(status = 200, message = "Request was successful", data = null))
        } else {
            val errorMessage = apiResponse.errorBody()?.string() ?: "Unknown error occurred"
            Response.error(apiResponse.code(), errorMessage.toResponseBody())
        }
    }

}