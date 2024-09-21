package com.example.doc_di.reminder.medication_reminder.viewmodel

import androidx.lifecycle.ViewModel
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
open class AddMedicationViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

//    open fun createMedications(
//        name: String,
//        dosage: Int,
//        recurrence: String,
//        endDate: Date,
//        medicationTimes: List<CalendarInformation>,
//        startDate: Date = Date()
//    ): List<Reminder> {
//
//        // Determine the recurrence interval based on the selected recurrence
//        val interval = when (recurrence) {
//            "Daily" -> 1
//            "Weekly" -> 7
//            "Monthly" -> 30
//            else -> throw IllegalArgumentException("Invalid recurrence: $recurrence")
//        }
//
//        val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
//        val numOccurrences = ((endDate.time + oneDayInMillis - startDate.time) / (interval * oneDayInMillis)).toInt() + 1
//
//        // Create a Medication object for each occurrence and add it to a list
//        val reminders = mutableListOf<Reminder>()
//        val calendar = Calendar.getInstance()
//        calendar.time = startDate
//        for (i in 0 until numOccurrences) {
//            for (medicationTime in medicationTimes) {
//                // TODO: Generate id automatically.
//                val reminder = Reminder(
//                    id = 0,
//                    name = name,
//                    dosage = dosage,
//                    recurrence = recurrence,
//                    endDate = endDate,
//                    medicationTaken = false,
//                    medicationTime = getMedicationTime(medicationTime, calendar)
//                )
//                reminders.add(reminder)
//            }
//
//            // Increment the date based on the recurrence interval
//            calendar.add(Calendar.DAY_OF_YEAR, interval)
//        }
//
//        return reminders
//    }

    private fun getMedicationTime(medicationTime: CalendarInformation, calendar: Calendar): Date {
        calendar.set(Calendar.HOUR_OF_DAY, medicationTime.dateInformation.hour)
        calendar.set(Calendar.MINUTE, medicationTime.dateInformation.minute)
        return calendar.time
    }

    open fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
}