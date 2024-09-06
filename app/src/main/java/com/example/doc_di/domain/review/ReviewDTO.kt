package com.example.doc_di.domain.review

data class ReviewDTO(
    val email: String,
    val name: String,
    val medicineName: String,
    val statistic: String,
    val date: String,
    val rate: Short
)
