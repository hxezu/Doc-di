package com.example.doc_di.reminder.data

data class AppointmentData(
    val appointmentId: Int,
    val hospitalName: String,
    val doctorName: String,
    val subject: String,
    val formattedTime: String // "2024.10.01 오전/오후 11:00"
)
