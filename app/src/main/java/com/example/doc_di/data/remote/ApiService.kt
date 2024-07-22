package com.example.doc_di.data.remote

import com.example.doc_di.data.model.ApiDelteTaskReq
import com.example.doc_di.data.model.ApiGetTaskReq
import com.example.doc_di.data.model.ApiSuccess
import com.example.doc_di.data.model.TaskResponse
import com.example.doc_di.domain.model.TaskRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/storeCalendarTask")
    suspend fun storeCalendarTask(@Body taskRequest: TaskRequest): ApiSuccess

    @POST("/api/getCalendarTaskList")
    suspend fun getCalendarTaskLists(@Body userId: ApiGetTaskReq): TaskResponse

    @POST("/api/deleteCalendarTask")
    suspend fun deleteCalendarTask(@Body apiDelteTaskReq: ApiDelteTaskReq): ApiSuccess
}