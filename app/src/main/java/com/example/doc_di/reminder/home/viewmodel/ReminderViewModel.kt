package com.example.doc_di.reminder.home.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.reminder.ReminderApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.*
import com.example.doc_di.domain.model.Booked
import retrofit2.Response

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderApi: ReminderApi
) : ViewModel() {

    private val _reminders = mutableStateOf<List<Reminder>>(emptyList())
    val reminders: State<List<Reminder>> = _reminders
    fun getReminderById(id: Int): Reminder? {
        return _reminders.value.find { it.id == id }
    }

    private val _bookedReminders = mutableStateOf<List<Booked>>(emptyList())
    val bookedReminders: State<List<Booked>> = _bookedReminders
    fun getBookedReminderById(id: Int): Booked? {
        return _bookedReminders.value.find { it.id == id }
    }

    fun getReminders(email: String) {
        viewModelScope.launch {
            try {
                val response = reminderApi.findReminder(email)
                Log.d("ReminderViewModel", "API Response: $response")
                _reminders.value = response.data
            } catch (e: Exception) {
                println("Failed to fetch reminders: ${e.message}")
            }
        }
    }


    fun deleteReminder(reminderId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = reminderApi.deleteReminder(reminderId)
                if (response.isSuccessful) {
                    _reminders.value = _reminders.value.filterNot { it.id?.toInt() == reminderId }
                } else {
                    Log.e("ReminderViewModel", "복용 알림 삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Failed to delete reminders: ${e.message}")
            }
        }
    }


    fun getBookedReminders(email: String) {
        viewModelScope.launch {
            try {
                val response = reminderApi.findBookedReminder(email)
                Log.d("ReminderViewModel", "API Response: $response")
                _bookedReminders.value = response.data
            } catch (e: Exception) {
                println("Failed to fetch reminders: ${e.message}")
            }
        }
    }

    fun deleteBookedReminder(boookedReminderId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = reminderApi.deleteBookedReminder(boookedReminderId)
                if (response.isSuccessful) {
                    _bookedReminders.value = _bookedReminders.value.filterNot { it.id?.toInt() == boookedReminderId }
                } else {
                    Log.e("ReminderViewModel", "진료 알림 삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Failed to delete Booked reminders: ${e.message}")
            }
        }
    }
}

