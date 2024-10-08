package com.example.doc_di.domain.register

import com.example.doc_di.domain.ServerResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RegisterApi {
    @Multipart
    @POST("join")
    suspend fun join(
        @Part joinDto: MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ): Response<Unit>

    @POST("user/requestcode")
    suspend fun makeCode(@Query("email") email: String): Response<ServerResponse<String>>

    @POST("user/checkcode")
    suspend fun checkCode(@Body checkCodeDTO: CheckCodeDTO): Response<ServerResponse<String>>
}