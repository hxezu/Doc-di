package com.example.doc_di.reminder.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.reminder.ReminderApi
import com.example.doc_di.reminder.data.AppointmentData
import com.example.doc_di.reminder.data.MedicationData
import com.example.doc_di.reminder.data.toAppointmentData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderApi: ReminderApi
) : ViewModel() {
    var reservedTreatment = false
    val isLoading = MutableLiveData<Boolean>(false)

    private val _reminders = mutableStateOf<List<Reminder>>(emptyList())
    val reminders: State<List<Reminder>> = _reminders
    private val _bookedReminders = mutableStateOf<List<Booked>>(emptyList())
    val bookedReminders: State<List<Booked>> = _bookedReminders
    private val _pills = mutableStateOf<List<Pill>>(emptyList())
    val pills: State<List<Pill>> = _pills


    //LiveData
    // Past appointments
    private val _pastAppointments = MutableLiveData<List<AppointmentData>>()
    val pastAppointments: LiveData<List<AppointmentData>> get() = _pastAppointments

    // Upcoming appointments
    private val _upcomingAppointments = MutableLiveData<List<AppointmentData>>()
    val upcomingAppointments: LiveData<List<AppointmentData>> get() = _upcomingAppointments

    // Medications for today
    private val _medicationsForToday = MutableLiveData<List<MedicationData>>()
    val medicationsForToday: LiveData<List<MedicationData>> get() = _medicationsForToday



    fun getReminderById(id: Int): Reminder? {
        return _reminders.value.find { it.id == id }
    }
    fun getBookedReminderById(id: Int): Booked? {
        return _bookedReminders.value.find { it.id == id }
    }

    fun getReminders(email: String) {
        viewModelScope.launch {
            try {
                val response = reminderApi.findReminder(email)
                Log.d("MedicationReminderViewModel", "API Response: $response")
                _reminders.value = response.data
                updateMedicationsForToday(response.data ?: emptyList(), emptyList())
            } catch (e: Exception) {
                println("Failed to fetch Medication reminders: ${e.message}")
            }
        }
    }


    fun deleteReminder(reminderId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = reminderApi.deleteReminder(reminderId)
                if (response.isSuccessful) {
                    _reminders.value = _reminders.value.filterNot { it.id?.toInt() == reminderId }
                    updateMedicationsForToday(_reminders.value, _pills.value)
                } else {
                    Log.e("ReminderViewModel", "복용 알림 삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Failed to delete reminders: ${e.message}")
            }
        }
    }


    fun getBookedReminders(email: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = reminderApi.findBookedReminder(email)
                Log.d("BookedReminderViewModel", "API Response: $response")
                _bookedReminders.value = response.data
                updateAppointments(response.data)
            } catch (e: Exception) {
                println("Failed to fetch Booked reminders: ${e.message}")
            }
            isLoading.value = false
        }
    }

    fun deleteBookedReminder(bookedReminderId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Unit> = reminderApi.deleteBookedReminder(bookedReminderId)
                if (response.isSuccessful) {
                    _bookedReminders.value = _bookedReminders.value.filterNot { it.id?.toInt() == bookedReminderId }
                    updateAppointments(_bookedReminders.value)
                } else {
                    Log.e("ReminderViewModel", "진료 알림 삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Failed to delete Booked reminders: ${e.message}")
            }
        }
    }

    // Helper method to update past and upcoming appointments based on current time
    fun updateAppointments(bookedReminders: List<Booked>) {
        val currentTime = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        // Past appointments
        val past = bookedReminders.filter { booked ->
            val bookedDate = try {
                dateFormat.parse(booked.bookTime)
            } catch (e: Exception) {
                null
            }
            bookedDate?.before(currentTime) == true
        }.sortedByDescending { booked ->
            dateFormat.parse(booked.bookTime)
        }.map {
            it.toAppointmentData()
        }
        _pastAppointments.value = past
        Log.d("AppointmentUpdate", "Past Appointments: $past") // Log for past appointments

        // Upcoming appointments
        val upcoming = bookedReminders.filter { booked ->
            val bookedDate = try {
                dateFormat.parse(booked.bookTime)
            } catch (e: Exception) {
                null
            }
            bookedDate?.after(currentTime) == true
        }.sortedBy { booked ->
            dateFormat.parse(booked.bookTime)
        }.map {
            it.toAppointmentData()
        }
        _upcomingAppointments.value = upcoming
        Log.d("AppointmentUpdate", "Upcoming Appointments: $upcoming") // Log for upcoming appointments
    }

    // Helper method to update medications for today
    fun updateMedicationsForToday(reminders: List<Reminder>, pills: List<Pill>) {
        val currentDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeFormat = SimpleDateFormat("a hh:mm", Locale("ko", "KR"))
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // medicationTime 형식에 맞춰 조정
        val endDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

        val todayMedications = reminders.filter { reminder ->
            val reminderDate = try {
                dateFormat.parse(reminder.medicationTime) // 수정된 부분
            } catch (e: Exception) {
                null
            }

            reminderDate?.let {
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                formattedDate == currentDateString
            } ?: false
        }.sortedBy { dateFormat.parse(it.medicationTime) }.map {
            val parsedEndDate = try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.endDate)
            } catch (e: Exception) {
                null
            }

            MedicationData(
                name = it.medicineName,
                time = timeFormat.format(dateFormat.parse(it.medicationTime) ?: Date()), // 기본값 설정
                endDate = parsedEndDate?.let { date ->
                    endDateFormat.format(date)
                } ?: "날짜 정보 없음",
                dosage = it.dosage
            )
        }

        Log.d("MedicationUpdate", "Today's Medications: $todayMedications") // 최종 결과 로그
        _medicationsForToday.value = todayMedications
    }
}