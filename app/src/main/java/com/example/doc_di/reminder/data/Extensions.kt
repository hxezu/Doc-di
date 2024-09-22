package com.example.doc_di.reminder.data

import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.reminder.calendar.navigation.AppointmentData
import java.text.SimpleDateFormat
import java.util.Locale

fun Booked.toAppointmentData(): AppointmentData {
    val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val targetFormat = SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.getDefault())

    val formattedTime = try {
        val date = originalFormat.parse(bookTime)
        date?.let { targetFormat.format(it) } ?: bookTime
    } catch (e: Exception) {
        bookTime
    }

    return AppointmentData(
        hospitalName = this.hospitalName,
        doctorName = this.doctorName,
        subject = this.subject,
        formattedTime = formattedTime
    )
}

fun Reminder.getClassName(): String {
    return "해열.진통.소염제"
}

fun Reminder.getRating(): Double {
    return 4.5
}