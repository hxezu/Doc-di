package com.example.doc_di.reminder.data

import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.Reminder
import java.text.SimpleDateFormat
import java.util.Locale

fun Booked.toAppointmentData(): AppointmentData {
    val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val targetFormat = SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale("ko", "KR"))

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

fun Reminder.getClassName(pills: List<Pill>): String {
    val matchingPill = pills.find { it.itemName == this.medicineName }
    return matchingPill?.className ?: "알 수 없음" // 일치하는 것이 없으면 "알 수 없음" 반환
}

fun Reminder.getRating(pills: List<Pill>): Double {
    val matchingPill = pills.find { it.itemName == this.medicineName }
    return matchingPill?.rateTotal?.toDouble() ?: 4.5 // 일치하는 것이 없으면 기본값 4.5 반환
}