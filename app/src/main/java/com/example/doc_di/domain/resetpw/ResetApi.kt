package com.example.doc_di.domain.resetpw

import com.example.doc_di.domain.ServerResponse
import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Query

interface ResetApi {
    @PUT("user/findpw")
    suspend fun resetPassword(@Query("email") email: String): Response<ServerResponse<String>>
}