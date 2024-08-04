package com.example.doc_di.management.home.usecase

import com.example.doc_di.domain.model.Medication
import com.example.doc_di.domain.repository.MedicationRepository
import javax.inject.Inject

class UpdateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    suspend fun updateMedication(medication: Medication) {
        return repository.updateMedication(medication)
    }
}