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
        dosage: String,
        recurrence: String,
        startDate: Date,
        endDate: Date,
        medicationTimes:  List<CalendarInformation>,
        medicationTaken : String,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val interval = when (recurrence) {
                        "선택 안함" -> null
                        "매일" -> 1
                        "매주" -> 7
                        "매달" -> 30
                        else -> throw IllegalArgumentException("Invalid recurrence: $recurrence")
                    }
                    val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day

                    // 시작 날짜 설정 (시간 부분은 0으로 설정)
                    val calendar = Calendar.getInstance().apply {
                        time = startDate
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    // 종료 날짜도 시간 부분을 0으로 설정
                    val endCalendar = Calendar.getInstance().apply {
                        time = endDate
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 99)
                    }

                    if (interval == null) {
                        // "선택 안함"일 때: 단일 알림만 생성
                        for (medicationTime in medicationTimes) {
                            val medicationTimeDate = getReminderTime(medicationTime, calendar)
                            val reminderDTO = ReminderDTO(
                                email = email,
                                medicineName = medicineName,
                                dosage = dosage,
                                recurrence = recurrence,
                                endDate = endDate.toFormattedDateString(),
                                medicationTime = medicationTimeDate.toFormattedDateTimeString(),
                                medicationTaken = medicationTaken
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
                    } else {
                        // "매일", "매주", "매달"일 때: 반복 알림 생성
                        val numOccurrences = ((endDate.time - startDate.time) / (interval * oneDayInMillis)).toInt() + 1
                        for (i in 0 until numOccurrences) {
                            for (medicationTime in medicationTimes) {
                                val medicationTimeDate = getReminderTime(medicationTime, calendar)

                                if (calendar.timeInMillis >= endCalendar.timeInMillis) {
                                    continue
                                }

                                val reminderDTO = ReminderDTO(
                                    email = email,
                                    medicineName = medicineName,
                                    dosage = dosage,
                                    recurrence = recurrence,
                                    endDate = endDate.toFormattedDateString(),
                                    medicationTime = medicationTimeDate.toFormattedDateTimeString(),
                                    medicationTaken = medicationTaken
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
                            calendar.add(Calendar.DAY_OF_YEAR, interval)
                        }
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



    private fun getReminderTime(reminderTime: CalendarInformation, calendar: Calendar): Date {
        calendar.set(Calendar.HOUR_OF_DAY, reminderTime.dateInformation.hour)
        calendar.set(Calendar.MINUTE, reminderTime.dateInformation.minute)
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
        recurrence: String,
        endDate: Date,
        bookTimes: List<CalendarInformation>,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if(recurrence == "선택 안함"){
                        for (bookTime in bookTimes) {
                            val bookTimeDate = getReminderTime(bookTime, Calendar.getInstance().apply {
                                time = startDate
                            })

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
                    }else{
                        val interval = when (recurrence) {
                            "매일" -> 1
                            "매주" -> 7
                            "매달" -> 30
                            else -> throw IllegalArgumentException("Invalid recurrence: $recurrence")
                        }

                        val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
                        val numOccurrences = ((endDate.time - startDate.time) / (interval * oneDayInMillis)).toInt() + 1


                        val calendar = Calendar.getInstance().apply {
                            time = startDate
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        // 종료 날짜도 시간 부분을 0으로 설정
                        val endCalendar = Calendar.getInstance().apply {
                            time = endDate
                            set(Calendar.HOUR_OF_DAY, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
                            set(Calendar.MILLISECOND, 99)
                        }

                        for (i in 0 until numOccurrences) {
                            for (bookTime in bookTimes) {
                                val bookTimeDate = getReminderTime(bookTime, calendar)

                                if (calendar.timeInMillis >= endCalendar.timeInMillis) {
                                    continue
                                }

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
                            calendar.add(Calendar.DAY_OF_YEAR, interval)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "예약 등록 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) {
                            navController.popBackStack()
                        }
                    }

                }catch (e:Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
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
    ): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val editBookedResponse = reminderApi.editBookedReminder(booked)

                if (editBookedResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "진료 알림 수정 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) {
                            navController.popBackStack()
                        }
                    }
                    true
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "진료 알림 수정 실패: ${editBookedResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                    false
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

}