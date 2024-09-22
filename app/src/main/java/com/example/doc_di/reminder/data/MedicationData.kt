package com.example.doc_di.reminder.data

data class MedicationData(
    val name: String,
    val time: String, // "오전/오후 10:00"
    val efficacy: String, // e.g., "해열.진통.소염제"
    val rating: String // e.g., "4.5"
)
