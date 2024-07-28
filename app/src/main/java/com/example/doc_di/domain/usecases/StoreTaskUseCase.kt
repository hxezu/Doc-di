package com.example.doc_di.domain.usecases

import com.example.doc_di.data.model.ApiSuccess
import com.example.doc_di.domain.model.PillTaskRequest
import com.example.doc_di.domain.repository.TaskRepository
import javax.inject.Inject

class StoreTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(pillTaskRequest: PillTaskRequest): ApiSuccess {
        return repository.storeTask(pillTaskRequest)
    }
}