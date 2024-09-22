package com.example.doc_di.reminder.medication_reminder.model

import androidx.compose.runtime.saveable.Saver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.MissingFormatArgumentException

class CalendarInformation(private val calendar: Calendar) {

    val dateInformation = TimeInformation(
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE)
    )

    fun getCalendar(): Calendar {
        return calendar
    }


    fun getTimeInMillis() = calendar.timeInMillis

//    fun getDateFormatted(pattern: String): String {
//        return try {
//            SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
//        } catch (ex: MissingFormatArgumentException) {
//            throw ex
//        }
//    }

    fun formatTimesToString(selectedTimes: List<CalendarInformation>): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return selectedTimes.joinToString(separator = ", ") { calendarInfo ->
            dateFormat.format(calendarInfo.getTimeInMillis())
        }
    }

    fun getDateFormatted(pattern: String): String {
        return try {
            val dateFormat = SimpleDateFormat(pattern, Locale.KOREAN)
            // Use DateFormatSymbols to get Korean AM/PM symbols
            val symbols = dateFormat.dateFormatSymbols
            symbols.amPmStrings = arrayOf("오전", "오후")
            dateFormat.dateFormatSymbols = symbols
            dateFormat.format(calendar.time)
        } catch (ex: MissingFormatArgumentException) {
            throw ex
        }
    }


    inner class TimeInformation(
        val hour: Int,
        val minute: Int,
    ) {
        operator fun component1(): Int = hour
        operator fun component2(): Int = minute
    }

    companion object {
        fun parseBookTimeToCalendarInformation(bookTime: String): CalendarInformation? {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val date = inputFormat.parse(bookTime)
                if (date != null) {
                    val calendar = Calendar.getInstance().apply { time = date }
                    CalendarInformation(calendar) // Return CalendarInformation instance
                } else {
                    null // Return null if parsing fails
                }
            } catch (e: Exception) {
                null // Return null if there's an exception
            }
        }

        fun getStateSaver() = Saver<CalendarInformation, Calendar>(
            save = { state ->
                state.calendar
            },
            restore = {
                CalendarInformation(it)
            }
        )

        fun getStateListSaver() = Saver<MutableList<CalendarInformation>, MutableList<Calendar>>(
            save = { state ->
                state.map { it.calendar }.toMutableList()
            },
            restore = {
                it.map { CalendarInformation(it) }.toMutableList()
            }
        )
    }
}