package com.example.doc_di.reminder.medicationconfirm.usecase

import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.repository.MedicationRepository
import javax.inject.Inject

class AddMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend fun addMedication(reminders: List<Reminder>) {
        repository.insertMedications(reminders)
    }
}