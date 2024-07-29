package com.example.doc_di.domain.pillsearch;

import com.example.practice.data.model.Pills
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("medicine/find")
    suspend fun getPillSearchListByName(
        @Query("name") name: String,
    ): Pills

    companion object {
        const val BASE_URL = "http://43.201.93.103:8080/"
    }
}
