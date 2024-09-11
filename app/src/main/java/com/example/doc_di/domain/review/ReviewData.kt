package com.example.doc_di.domain.review

data class ReviewData (
    val id: Int,
    val email: String,
    val name: String,
    val medicineName: String,
    val statistic: String,
    val date: String,
    val rate: Short
)