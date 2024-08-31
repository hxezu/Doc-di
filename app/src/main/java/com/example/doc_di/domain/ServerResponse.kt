package com.example.doc_di.domain

data class ServerResponse<T>(
    val status: Int,
    val message: String,
    val data: T
)