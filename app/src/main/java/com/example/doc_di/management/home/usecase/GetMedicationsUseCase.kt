package com.example.doc_di.management.home.usecase

import com.example.doc_di.domain.model.Medication
import com.example.doc_di.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    fun getMedications(date: String? = null): Flow<List<Medication>> {
        return if (date != null) {
            repository.getMedicationsForDate(date)
        } else repository.getAllMedications()
    }
}