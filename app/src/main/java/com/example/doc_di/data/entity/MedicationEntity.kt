package com.example.doc_di.data.entity

import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val medicationTime: Date = Date(),
    val medicationTaken: Boolean,
)