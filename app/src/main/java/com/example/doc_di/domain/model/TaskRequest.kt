package com.example.doc_di.domain.model

import com.google.gson.annotations.SerializedName
import com.example.doc_di.domain.model.TaskModel

data class TaskRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task")
    val task: TaskModel
)