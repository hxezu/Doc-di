package com.example.doc_di.domain.reminder

data class ReminderDTO(
    val email : String,
    val medicineName: String,
    val dosage: String,
    val recurrence: String,
    val endDate: String,
    val medicationTime: String,
    val medicationTaken: String,
)
