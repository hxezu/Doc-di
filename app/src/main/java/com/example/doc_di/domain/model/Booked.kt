package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Booked(
    val id: Int,
    val hospitalName: String,
    val doctorName: String,
    val subject: String,
    val bookTime: String,
) : Parcelable{

}