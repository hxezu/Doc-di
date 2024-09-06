package com.example.doc_di.domain.pill

import com.example.doc_di.domain.model.PillInfos
import com.example.doc_di.domain.model.Pills
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PillApi {
    @POST("medicine/find")
    suspend fun getPillSearchList(@Body options: Map<String, String>): Pills

    @GET("medicine/info")
    suspend fun getPillInfo(@Query("name") name: String): PillInfos
}
