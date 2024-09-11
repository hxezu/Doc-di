package com.example.doc_di.domain.reminder

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReminderApi {
    @POST("reminder/create")
    suspend fun createReminder(
        @Body reminderDto: ReminderDTO,
    ): Response<Unit>
}