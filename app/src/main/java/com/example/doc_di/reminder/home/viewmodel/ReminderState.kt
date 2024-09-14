package com.example.doc_di.reminder.home.viewmodel

import com.example.doc_di.domain.model.Reminder

data class ReminderState(
    val greeting: String = "",
    val userName: String = "",
    val lastSelectedDate: String = "",
    val reminders: List<Reminder> = emptyList()
)