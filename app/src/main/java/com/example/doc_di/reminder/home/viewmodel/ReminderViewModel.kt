package com.example.doc_di.reminder.home.viewmodel

import android.util.Log
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
                val response = reminderApi.findReminder(email)
                Log.d("ReminderViewModel", "API Response: $response")
                val remindersList = response.data.mapNotNull { convertToReminder(it) }
                _reminders.value = remindersList
            } catch (e: Exception) {
                println("Failed to fetch reminders: ${e.message}")
            }
        }
    }
}

// ViewModel function to convert the DTO to domain model
fun convertToReminder(dto: ReminderDTO): Reminder? {
    val medicationTimeDate = dto.medicationTime.toDate()
    val endDateDate = dto.endDate.toDate()

    return if (medicationTimeDate != null && endDateDate != null) {
        Reminder(
            id = null, // Set the id if available
            name = dto.medicineName,
            dosage = dto.dosage.toInt(),
            recurrence = dto.recurrence,
            endDate = endDateDate, // Assuming endDate is in correct format
            medicationTaken = dto.medicationTaken.toBoolean(),
            medicationTime = medicationTimeDate
        )
    } else {
        println("Conversion failed for DTO: $dto")
        null
    }
}
