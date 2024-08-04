package com.example.doc_di.data.repository

import com.example.doc_di.data.MedicationDao
import com.example.doc_di.data.mapper.toMedication
import com.example.doc_di.data.mapper.toMedicationEntity
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicationRepositoryImpl(
    private val dao: MedicationDao
) : MedicationRepository {

    override suspend fun insertMedications(medications: List<Medication>) {
        medications.map { it.toMedicationEntity() }.forEach {
            dao.insertMedication(it)
        }
    }

    override suspend fun deleteMedication(medication: Medication) {
        dao.deleteMedication(medication.toMedicationEntity())
    }

    override suspend fun updateMedication(medication: Medication) {
        dao.updateMedication(medication.toMedicationEntity())
    }

    override fun getAllMedications(): Flow<List<Medication>> {
        return dao.getAllMedications().map { entities ->
            entities.map { it.toMedication() }
        }
    }

    override fun getMedicationsForDate(date: String): Flow<List<Medication>> {
        return dao.getMedicationsForDate(
            date = date
        ).map { entities ->
            entities.map { it.toMedication() }
        }
    }
}
