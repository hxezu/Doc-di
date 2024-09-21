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
import retrofit2.Response

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
                _reminders.value = response.data
            } catch (e: Exception) {
                println("Failed to fetch reminders: ${e.message}")
            }
        }
    }

    fun getReminderById(id: Int): Reminder? {
        return _reminders.value.find { it.id == id }
    }

    fun deleteReminder(reminderId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = reminderApi.deleteReminder(reminderId)
                if (response.isSuccessful) {
                    _reminders.value = _reminders.value.filterNot { it.id?.toInt() == reminderId }
                } else {
                    Log.e("ReminderViewModel", "알림 삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Failed to delete reminders: ${e.message}")
            }
        }
    }

    fun editReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = reminderApi.editReminder(reminder)
                if (response.isSuccessful) {
                    _reminders.value = _reminders.value.map {
                        if (it.id == reminder.id) reminder else it
                    }
                    Log.d("ReminderViewModel", "알림 수정 성공")
                } else {
                    Log.e("ReminderViewModel", "알림 수정 실패: ${response.errorBody()?.string()}")
                }

            }catch (e: Exception){
                println("Failed to edit reminders: ${e.message}")
            }
        }
    }
}

