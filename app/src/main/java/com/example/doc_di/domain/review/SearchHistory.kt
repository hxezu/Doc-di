package com.example.doc_di.domain.review

data class SearchHistory(
    val id: Int,
    val email: String,
    val medicineName: String,
    val itemSeq: String,
    val searchTime: String,
)
