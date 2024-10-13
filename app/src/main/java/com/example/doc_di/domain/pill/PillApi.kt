package com.example.doc_di.domain.pill

import com.example.doc_di.domain.ServerResponse
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.PillInfos
import com.example.doc_di.domain.model.Pills
import com.example.doc_di.domain.review.SearchHistory
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface PillApi {
    @POST("medicine/find")
    suspend fun getPillSearchList(@Body options: Map<String, String>): Pills

    @POST("medicine/info")
    suspend fun getPillInfo(@Body searchHistoryDto: SearchHistoryDto): PillInfos

    @PUT("medicine/statistics")
    suspend fun modifyRateInfo(@Body rateInfo: RateInfo): Response<Unit>

    @Multipart
    @POST("medicine/image")
    suspend fun getPillListByImage(
        @Part image: MultipartBody.Part
    ): Response<ServerResponse<List<Pill>>>

    @GET("history/get")
    suspend fun getPillHistory(
        @Header("access") accessToken: String,
        @Query("email") email: String
    ): Response<ServerResponse<List<SearchHistory>>>

    @DELETE("history/delete")
    suspend fun deleteHistory(
        @Header("access") accessToken: String,
        @Query("id") id:Int
    ): Response<ServerResponse<String>>
}
