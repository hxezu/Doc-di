package com.example.doc_di.domain.reminder

import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Reminder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("reminder/medicine/edit")
    suspend fun editReminder(
        @Body reminder: Reminder
    ):Response<Unit>

    @GET("reminder/booked/find")
    suspend fun findBookedReminder(
        @Query("email") email: String
    ) : BookedResponse

    @POST("reminder/booked/create")
    suspend fun createBookedReminder(
        @Body bookedDTO: BookedDTO,
    ):  Response<Unit>

    @DELETE("reminder/booked/delete")
    suspend fun deleteBookedReminder(
        @Query("id") id: Int
    ): Response<Unit>

    @PUT("reminder/booked/edit")
    suspend fun editBookedReminder(
        @Body booked: Booked
    ):Response<Unit>
}