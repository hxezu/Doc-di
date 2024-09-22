package com.example.doc_di.reminder.calendar.navigation

data class AppointmentData(
    val hospitalName: String,
    val doctorName: String,
    val subject: String,
    val formattedTime: String // "2024.10.01 오전/오후 11:00"
)
