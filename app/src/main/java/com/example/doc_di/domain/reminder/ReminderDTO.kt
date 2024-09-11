package com.example.doc_di.domain.reminder

data class ReminderDTO(
    val email : String,
    val medicineName: String,
    val medicineUnit: String,
    val oneTimeAmount: Short,
    val oneTimeCount: Short,
    val eatingDays: Short,
    val eatingStartDate: String,
)
