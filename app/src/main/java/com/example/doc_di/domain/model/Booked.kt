package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Booked(
    val id: Int?,
    val hospitalName: String,
    val doctorName: String,
    val subject: String,
    val bookTime: String,
) : Parcelable{

    fun getFormattedBookTime(): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy.MM.dd a hh:mm", Locale.getDefault())

        return try {
            val date = originalFormat.parse(bookTime)
            date?.let { targetFormat.format(it) } ?: bookTime
        } catch (e: Exception) {
            // 파싱 실패 시 원래 bookTime 반환
            bookTime
        }
    }
}