package com.example.doc_di.domain.reminder

import com.example.doc_di.domain.model.Reminders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReminderApi {
    @POST("reminder/medicine/create")
    suspend fun createReminder(
        @Body reminderDto: ReminderDTO,
    ): Response<Unit>

    @GET("reminder/medicine/find")
    suspend fun findReminder(
        @Query("email") email: String
    ) : ReminderResponse
}