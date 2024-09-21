package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Reminder(
    val medicineName: String,
    val id: Long?,
    val dosage: Int,
    val recurrence: String,
    val endDate: String,
    val medicationTaken: Boolean,
    val medicationTime: String
) : Parcelable