package com.example.doc_di.domain.review

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReviewApi {
    @POST("statistic/create")
    suspend fun createReview(
        @Header("access") accessToken: String,
        @Body reviewDTO: ReviewDTO,
    ): Response<Unit>

}