package com.example.doc_di.data.mapper

import com.example.doc_di.data.entity.MedicationEntity
import com.example.doc_di.domain.model.Reminder

fun MedicationEntity.toMedication(): Reminder {
    return Reminder(
        id = id,
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate,
        medicationTime = medicationTime,
        medicationTaken = medicationTaken
    )
}

fun Reminder.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        id = id ?: 0L,
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate,
        medicationTime = medicationTime,
        medicationTaken = medicationTaken
    )
}