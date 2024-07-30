package com.example.doc_di.data.model

data class TaskModel(
    val title: String,
    val description: String,
    val date: String? = null,
    val time: String? = null
)