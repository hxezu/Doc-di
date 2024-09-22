package com.example.doc_di.domain.reminder

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.etc.Routes
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.extension.toFormattedDateTimeString
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class ReminderImpl(private val reminderApi: ReminderApi) {
    suspend fun createReminder(
        email: String,
        medicineName: String,
        dosage: Short,
        recurrence: String,
        startDate: Date,
        endDate: Date,
        medicationTimes:  List<CalendarInformation>,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val interval = when (recurrence) {
                        "매일" -> 1
                        "매주" -> 7
                        "매달" -> 30
                        else -> throw IllegalArgumentException("Invalid recurrence: $recurrence")
                    }
                    val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
                    val numOccurrences = ((endDate.time + oneDayInMillis - startDate.time) / (interval * oneDayInMillis)).toInt() + 1

                    // Create a Medication object for each occurrence
                    val calendar = Calendar.getInstance().apply { time = startDate }

                    for (i in 0 until numOccurrences) {
                        for (medicationTime in medicationTimes) {
                            val medicationTimeDate = getMedicationTime(medicationTime, calendar)
                            val reminderDTO = ReminderDTO(
                                email = email,
                                medicineName = medicineName,
                                dosage = dosage,
                                recurrence = recurrence,
                                endDate = endDate.toFormattedDateString(),
                                medicationTime = medicationTimeDate.toFormattedDateTimeString(),
                                medicationTaken = "false"
                            )

                            val reminderResponse = reminderApi.createReminder(reminderDTO)
                            if (!reminderResponse.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    println("알림 등록 실패")
                                    Toast.makeText(context, "알림 등록 실패", Toast.LENGTH_SHORT).show()
                                    return@withContext
                                }
                            }
                        }
                        // Increment the date by the recurrence interval (daily/weekly/monthly)
                        calendar.add(Calendar.DAY_OF_YEAR, interval)
                    }
                    withContext(Dispatchers.Main) {
                        println("알림 등록 성공")
                        Toast.makeText(context, "알림 등록 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) {
                            navController.popBackStack()
                        }
                    }

                }catch (e:Exception){
                    withContext(Dispatchers.Main) {
                        println("오류 발생: ${e.message}")
                        Toast.makeText(context, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "모든 정보를 정확히 기입해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun editReminder(
        reminder: Reminder,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        try {
            withContext(Dispatchers.IO) {
                val editResponse = reminderApi.editReminder(reminder)

                if (editResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "알림 수정 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) {
                            navController.popBackStack()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "알림 수정 실패: ${editResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun getMedicationTime(medicationTime: CalendarInformation, calendar: Calendar): Date {
        calendar.set(Calendar.HOUR_OF_DAY, medicationTime.dateInformation.hour)
        calendar.set(Calendar.MINUTE, medicationTime.dateInformation.minute)
        return calendar.time
    }

    private fun getFormattedBookTime(bookTime: CalendarInformation): String {
        // Create a Calendar instance for the desired time
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, bookTime.dateInformation.hour)
            set(Calendar.MINUTE, bookTime.dateInformation.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time.toFormattedDateTimeString() // Adjust as necessary
    }

    suspend fun createBookedReminder(
        email: String,
        hospitalName: String,
        doctorName: String,
        subject: String,
        startDate: Date,
        bookTimes: List<CalendarInformation>,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val calendar = Calendar.getInstance().apply { time = startDate }
                    for (bookTime in bookTimes) {
                        val bookTimeDate = getMedicationTime(bookTime, calendar)

                        val bookedDTO = BookedDTO(
                            email = email,
                            hospitalName = hospitalName,
                            doctorName = doctorName,
                            subject = subject,
                            bookTime = bookTimeDate.toFormattedDateTimeString()
                        )
                        val bookedResponse = reminderApi.createBookedReminder(bookedDTO)

                        if (!bookedResponse.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "예약 등록 실패: ${bookedResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                return@withContext
                            }
                        }
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "예약 등록 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) {
                            navController.popBackStack()
                        }
                    }

                }catch (e:Exception){

                }
            }
        } else {
            Toast.makeText(context, "모든 정보를 정확히 기입해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun editBookedReminder(
        booked: Booked,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        try {
            withContext(Dispatchers.IO) {
                val editBookedResponse = reminderApi.editBookedReminder(booked)

                if (editBookedResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "진료 알림 수정 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) {
                            navController.popBackStack()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "진료 알림 수정 실패: ${editBookedResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}