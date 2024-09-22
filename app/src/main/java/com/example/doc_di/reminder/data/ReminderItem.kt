package com.example.doc_di.reminder.data

import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Reminder

sealed class ReminderItem {
    sealed class ReminderType {
        data class Medication(val reminder: Reminder) : ReminderItem()
        data class Clinic(val booked: Booked) : ReminderItem()
    }
}