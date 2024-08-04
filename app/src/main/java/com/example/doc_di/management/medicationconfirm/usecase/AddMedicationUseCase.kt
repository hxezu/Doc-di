package com.example.doc_di.management.medicationconfirm.usecase

import com.example.doc_di.domain.model.Medication
import com.example.doc_di.domain.repository.MedicationRepository
import javax.inject.Inject

class AddMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend fun addMedication(medications: List<Medication>) {
        repository.insertMedications(medications)
    }
}