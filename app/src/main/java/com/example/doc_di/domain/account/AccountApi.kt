package com.example.doc_di.domain.account

import com.example.doc_di.domain.ServerResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface AccountApi {
    @POST("reissue")
    suspend fun reissueToken(): Response<Unit>

    @POST("logout")
    suspend fun logout(
        @Header("access") accessToken: String
    ): Response<Unit>

    @GET("user/find")
    suspend fun getUserInfo(
        @Query("email") email: String,
        @Header("access") accessToken: String
    ): Response<ServerResponse<AccountDTO>>

    @GET("data")
    suspend fun getUserImage(
        @Query("fileName") filename: String,
        @Header("access") accessToken: String
    ): Response<ResponseBody>

    @Multipart
    @PUT("user/edit")
    suspend fun modifyProfile(
        @Part userDto: MultipartBody.Part,
        @Part file: MultipartBody.Part,
        @Header("access") accessToken: String
    ): Response<ServerResponse<AccountDTO>>

    @DELETE("user/delete")
    suspend fun deleteAccount(
        @Query("email") email: String,
        @Header("access") accessToken: String
    ): Response<Unit>
}