package com.example.doc_di.domain.model

import com.google.gson.annotations.SerializedName

data class PillTaskRequest(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task")
    val task: PillTaskModel
)