package com.example.doc_di.domain.usecases

import com.example.doc_di.data.model.Task
import com.example.doc_di.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(userId: Int): List<Task> {
        return repository.getTaskList(userId)
    }
}