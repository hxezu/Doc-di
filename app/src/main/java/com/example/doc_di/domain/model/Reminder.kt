package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Reminder(
    val id: Int?,
    val medicineName: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: String,
    val medicationTaken: Boolean,
    val medicationTime: String
) : Parcelable