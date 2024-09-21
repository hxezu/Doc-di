package com.example.doc_di.domain.reminder

import com.example.doc_di.domain.model.Reminder


data class ReminderResponse(
    val success: Boolean,
    val data: List<Reminder>
)