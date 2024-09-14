package com.example.doc_di.reminder.home.usecase

import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.repository.MedicationRepository
import javax.inject.Inject

class UpdateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    suspend fun updateMedication(reminder: Reminder) {
        return repository.updateMedication(reminder)
    }
}