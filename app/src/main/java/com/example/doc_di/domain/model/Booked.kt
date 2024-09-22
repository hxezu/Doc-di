package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booked(
    val id: Int?,
    val hospitalName: String,
    val doctorName: String,
    val subject: String,
    val bookTime: String,
) : Parcelable