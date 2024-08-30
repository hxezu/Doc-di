package com.example.doc_di.domain.pillsearch

import com.example.doc_di.domain.model.PillInfos
import com.example.doc_di.domain.model.Pills
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {
    @GET("medicine/find")
    suspend fun getPillSearchList(@QueryMap options: Map<String, String>): Pills

    @GET("medicine/info")
    suspend fun getPillInfo(@Query("name") name: String): PillInfos
}
