package com.example.doc_di.domain.reminder

data class BookedDTO(
    val email : String,
    val hospitalName: String,
    val doctorName: String,
    val subject: String,
    val bookTime: String,
)
