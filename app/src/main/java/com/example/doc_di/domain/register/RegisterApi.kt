package com.example.doc_di.domain.register

import com.example.doc_di.domain.ServerResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegisterApi {
    @Multipart
    @POST("join")
    suspend fun join(
        @Part joinDto: MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ): Response<ServerResponse<JoinDTO>>

}