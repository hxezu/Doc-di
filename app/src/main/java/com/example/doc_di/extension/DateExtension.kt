package com.example.doc_di.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Convert Date to a formatted string suitable for display (e.g., "Thursday, November 06")
fun Date.toFormattedDisplayDateString(): String {
    val sdf = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
    return sdf.format(this)
}

// Convert Date to a formatted string with both date and time (e.g., "2024-10-06 11:00")
fun Date.toFormattedDateTimeString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(this)
}

// Convert Date to a formatted string with month and day (e.g., "November 06")
fun Date.toFormattedMonthDayString(): String {
    val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault())
    return sdf.format(this)
}

// Convert 24-hour time string to 12-hour format (e.g., "13:00" to "01:00 PM")
fun String.toFormattedTime(): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return try {
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: ParseException) {
        e.printStackTrace()
        this
    }
}

// Convert String to Date object (e.g., "2024-10-06" to Date)
fun String.toDate(): Date? {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        sdf.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

// Convert String to Date object (e.g., "2024-10-06" to Date)
fun String.toDateTime(): Date? {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return try {
        sdf.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

// Convert Date to a formatted string for Korean display (e.g., "11월 06일")
fun Date.toFormattedKoreanDateString(): String {
    val sdf = SimpleDateFormat("M월 d일", Locale.KOREA)
    return sdf.format(this)
}

// Convert Date to a formatted string for API requests (e.g., "2024-10-06")
fun Date.toApiDateFormat(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

// Convert Date to a formatted string for API requests with time (e.g., "24-10-06 11:00")
fun Date.toApiDateTimeFormat(): String {
    val sdf = SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(this)
}

// Convert Date to a short formatted date string (e.g., "06")
fun Date.toFormattedDateShortString(): String {
    val sdf = SimpleDateFormat("dd", Locale.getDefault())
    return sdf.format(this)
}

// Convert Long timestamp to formatted date string (e.g., "2024년 10월 06일")
fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    return sdf.format(this)
}

// Convert Date to a formatted time string (e.g., "13:00")
fun Date.toFormattedTimeString(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}

fun Date.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

// Check if the date has passed (i.e., if it is before the current time)
fun Date.hasPassed(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.SECOND, -1)
    val oneSecondAgo = calendar.time
    return time < oneSecondAgo.time
}

fun Date.toServerDateFormat(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.toServerTimeFormat(): String {
    val timeFormat = SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}

