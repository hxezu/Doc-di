package com.example.doc_di.domain.register

data class JoinResponse<T>(
    val status: Int,
    val message: String,
    val data: T
)