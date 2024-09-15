package com.example.doc_di.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Reminder(
    val name: String,
    val id: Long?,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val medicationTaken: Boolean,
    val medicationTime: Date
) : Parcelable