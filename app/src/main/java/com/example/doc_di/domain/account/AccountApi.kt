package com.example.doc_di.domain.account

import com.example.doc_di.domain.ServerResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface AccountApi {
    @Multipart
    @PUT("user/edit")
    suspend fun modifyProfile(
        @Part userDto: MultipartBody.Part,
        @Part file: MultipartBody.Part,
    ): Response<ServerResponse<AccountDTO>>
}