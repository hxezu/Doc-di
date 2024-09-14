package com.example.doc_di.reminder.home.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.reminder.ReminderDTO
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.reminder.ReminderApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import com.example.doc_di.domain.reminder.ReminderResponse
import com.example.doc_di.extension.toDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderApi: ReminderApi
) : ViewModel() {

    private val _reminders = mutableStateOf<List<Reminder>>(emptyList())
    val reminders: State<List<Reminder>> = _reminders

    fun getReminders(email: String) {
        viewModelScope.launch {
            try {
                val remindersResponse: ReminderResponse = reminderApi.findReminder(email)
                if (remindersResponse.success) {
                    _reminders.value = remindersResponse.data.mapNotNull { convertToReminder(it) } // Update the state with the list of reminders
                } else {
                    println("No reminders found or operation unsuccessful")
                }
            } catch (e: Exception) {
                println("Failed to fetch reminders: ${e.message}")
            }
        }
    }
}

// Extension function to parse time strings into Date
fun String.toTimeDate(): Date? {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return try {
        timeFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// ViewModel function to convert the DTO to domain model
fun convertToReminder(dto: ReminderDTO): Reminder? {
    val medicationTimeDate = dto.medicationTime.toTimeDate()
    return if (medicationTimeDate != null) {
        Reminder(
            id = null, // Set the id if available
            name = dto.medicineName,
            dosage = dto.dosage.toInt(),
            recurrence = dto.recurrence,
            endDate = dto.endDate.toDate() ?: Date(), // Assuming endDate is in correct format
            medicationTaken = dto.medicationTaken.toBoolean(),
            medicationTime = medicationTimeDate
        )
    } else {
        null
    }
}
