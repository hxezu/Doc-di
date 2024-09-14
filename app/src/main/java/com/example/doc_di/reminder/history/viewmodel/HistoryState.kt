package com.example.doc_di.reminder.history.viewmodel

import com.example.doc_di.domain.model.Reminder

data class HistoryState(
    val reminders: List<Reminder> = emptyList()
)