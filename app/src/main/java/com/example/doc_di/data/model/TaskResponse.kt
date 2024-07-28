package com.example.doc_di.data.model

data class TaskResponse(
    val tasks: List<Task>
)

data class Task(
    val task_id: Int,
    val task_detail: PillTaskModel
)