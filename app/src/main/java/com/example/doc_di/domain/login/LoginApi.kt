package com.example.doc_di.domain.login

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<Unit>
}