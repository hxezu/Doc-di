package com.example.doc_di.domain.reminder

import com.example.doc_di.domain.model.Reminders

data class ReminderResponse(
    val success: Boolean,
    val data: List<ReminderDTO>
)