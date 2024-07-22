package com.example.doc_di.data.repository

import android.util.Log
import com.example.doc_di.data.model.ApiDelteTaskReq
import com.example.doc_di.data.model.ApiGetTaskReq
import com.example.doc_di.data.model.ApiSuccess
import com.example.doc_di.data.model.Task
import com.example.doc_di.data.remote.ApiService
import com.example.doc_di.domain.model.TaskRequest
import com.example.doc_di.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val apiService: ApiService) : TaskRepository {

    override suspend fun storeTask(taskRequest: TaskRequest): ApiSuccess {
        return try {
            apiService.storeCalendarTask(taskRequest)
        } catch (e: Exception) {
            Log.i("--Repository--", "AddTaskAPI >> ${e.message}" )
            ApiSuccess(e.message.toString())
        }
    }

    override suspend fun getTaskList(userId: Int): List<Task> {
        return try {
            val response = apiService.getCalendarTaskLists(ApiGetTaskReq(userId))
            response.tasks
        } catch (e: Exception) {
            Log.i("--Repository--", "GetTaskAPI >> ${e.message}" )
            listOf<Task>()
        }
    }

    override suspend fun deleteTask(userId: Int, taskId: Int): ApiSuccess {
        return try {
            apiService.deleteCalendarTask(ApiDelteTaskReq(userId, taskId))
        } catch (e: Exception) {
            Log.i("--Repository--", "DeleteTaskAPI >> ${e.message}" )
            ApiSuccess(e.message.toString())
        }
    }
}