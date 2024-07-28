package com.example.doc_di.domain.repository

import com.example.doc_di.data.model.ApiSuccess
import com.example.doc_di.data.model.Task
import com.example.doc_di.domain.model.PillTaskRequest

interface TaskRepository {
    suspend fun storeTask(pillTaskRequest: PillTaskRequest): ApiSuccess
    suspend fun getTaskList(userId: Int): List<Task>
    suspend fun deleteTask(userId: Int, taskId: Int): ApiSuccess
}