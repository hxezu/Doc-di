package com.example.doc_di.reminder.home.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.reminder.ReminderApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderApi: ReminderApi
) : ViewModel() {

    private val _reminders = mutableStateOf<List<Reminder>>(emptyList())
    val reminders: State<List<Reminder>> = _reminders

    fun getReminders(email: String) {
        viewModelScope.launch {
            try {
                val remindersResponse = reminderApi.findReminder(email)
                if (remindersResponse.success) {
                    _reminders.value = remindersResponse.data // Update the state with the list of reminders
                } else {
                    println("No reminders found or operation unsuccessful")
                }
            } catch (e: Exception) {
                println("Failed to fetch reminders: ${e.message}")
            }
        }
    }
}