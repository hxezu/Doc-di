package com.example.doc_di.domain.reminder

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReminderApi {
    @POST("reminder/medicine/create")
    suspend fun createReminder(
        @Body reminderDto: ReminderDTO,
    ):  Response<Unit>

    @GET("reminder/medicine/find")
    suspend fun findReminder(
        @Query("email") email: String
    ) : ReminderResponse

    @DELETE("reminder/medicine/delete")
    suspend fun deleteReminder(
        @Query("id") id: Int
    ): Response<Unit>
}