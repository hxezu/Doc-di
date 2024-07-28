package com.example.doc_di.data.remote

import com.example.doc_di.data.model.ApiDelteTaskReq
import com.example.doc_di.data.model.ApiGetTaskReq
import com.example.doc_di.data.model.ApiSuccess
import com.example.doc_di.data.model.TaskResponse
import com.example.doc_di.domain.model.PillTaskRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/storePillTask")
    suspend fun storeCalendarTask(@Body pillTaskRequest: PillTaskRequest): ApiSuccess

    @POST("/api/getPillTaskList")
    suspend fun getCalendarTaskLists(@Body userId: ApiGetTaskReq): TaskResponse

    @POST("/api/deletePillTask")
    suspend fun deleteCalendarTask(@Body apiDelteTaskReq: ApiDelteTaskReq): ApiSuccess
}