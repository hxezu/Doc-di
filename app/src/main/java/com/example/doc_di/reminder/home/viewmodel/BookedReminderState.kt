package com.example.doc_di.reminder.home.viewmodel

import com.example.doc_di.domain.model.Booked

data class BookedReminderState(
    val greeting: String = "",
    val userName: String = "",
    val lastSelectedDate: String = "",
    val bookedReminders: List<Booked> = emptyList()
)