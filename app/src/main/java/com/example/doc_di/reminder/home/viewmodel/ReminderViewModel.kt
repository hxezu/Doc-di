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
                Log.e("ReminderViewModel", "알림 삭제 중 오류 발생: ${e.message}")
            }
        }
    }
}

//ViewModel function to convert the DTO to domain model
//fun convertToReminder(dto: ReminderDTO): Reminder? {
//    val medicationTimeDate = dto.medicationTime.toDateTime()
//    val endDateDate = dto.endDate.toDate()
//
//    return if (medicationTimeDate != null && endDateDate != null) {
//        Reminder(
//            id = null,
//            name = dto.medicineName,
//            dosage = dto.dosage.toInt(),
//            recurrence = dto.recurrence,
//            endDate = endDateDate, // Assuming endDate is in correct format
//            medicationTaken = dto.medicationTaken.toBoolean(),
//            medicationTime = medicationTimeDate
//        )
//    } else {
//        println("Conversion failed for DTO: $dto")
//        null
//    }
//}

fun generateNewId(): Long {
    return System.currentTimeMillis() // Example placeholder
}
