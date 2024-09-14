package com.example.doc_di.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("EEEE, LLLL dd", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toFormattedMonthDateString(): String {
    val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault())
    return sdf.format(this)
}

fun String.toFormattedTime(): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return try {
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        // Handle parsing error
        this
    }
}

fun Date.toFormattedKoreanDateString(): String {
    val sdf = SimpleDateFormat("M월 d일", Locale.KOREA)
    return sdf.format(this)
}


fun Date.toFormattedYearMonthDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

fun String.toDate(): Date? {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Date.toFormattedDateShortString(): String {
    val sdf = SimpleDateFormat("dd", Locale.getDefault())
    return sdf.format(this)
}

fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toFormattedTimeString(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}


fun Date.hasPassed(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.SECOND, -1)
    val oneSecondAgo = calendar.time
    return time < oneSecondAgo.time
}
