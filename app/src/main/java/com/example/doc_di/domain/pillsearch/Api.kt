package com.example.doc_di.domain.pillsearch

import com.example.doc_di.domain.model.JoinDTO
import com.example.doc_di.domain.model.JoinResponse
import com.example.doc_di.domain.model.LoginReq
import com.example.doc_di.domain.model.PillInfos
import com.example.doc_di.domain.model.Pills
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    @GET("medicine/find")
    suspend fun getPillSearchList(@QueryMap options: Map<String, String>): Pills

    @GET("medicine/info")
    suspend fun getPillInfo(@Query("name") name: String): PillInfos

    @POST("join")
    suspend fun join(@Body joinDTO: JoinDTO): Response<JoinResponse>

    @POST("login")
    suspend fun login(@Body loginReq: LoginReq): Response<Unit>
}
