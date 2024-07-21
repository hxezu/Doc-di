package com.example.doc_di.domain.usecases

import com.example.doc_di.data.model.ApiSuccess
import com.example.doc_di.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(userId: Int, taskId: Int): ApiSuccess {
        return repository.deleteTask(userId, taskId)
    }
}