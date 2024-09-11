package com.example.doc_di.domain.review

import com.example.doc_di.domain.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ReviewApi {
    @POST("statistic/create")
    suspend fun createReview(
        @Header("access") accessToken: String,
        @Body reviewDTO: ReviewDTO,
    ): Response<Unit>

    @GET("statistic/find")
    suspend fun findReview(
        @Header("access") accessToken: String,
        @Query("medicineName") medicineName: String,
    ):Response<ServerResponse<List<ReviewData>>>

    @PUT("statistic/edit")
    suspend fun editReview(
        @Header("access") accessToken: String,
        @Body reviewData: ReviewData
    ): Response<Unit>

    @DELETE("statistic/delete")
    suspend fun deleteReview(
        @Header("access") accessToken: String,
        @Query("id") id:Int
    ): Response<Unit>
}