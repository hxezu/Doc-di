package com.example.doc_di.domain.reminder


data class ReminderResponse(
    val success: Boolean,
    val data: List<ReminderDTO>
)