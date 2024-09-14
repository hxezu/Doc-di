package com.example.doc_di.data.repository

import com.example.doc_di.data.MedicationDao
import com.example.doc_di.data.mapper.toMedication
import com.example.doc_di.data.mapper.toMedicationEntity
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicationRepositoryImpl(
    private val dao: MedicationDao
) : MedicationRepository {

    override suspend fun insertMedications(reminders: List<Reminder>) {
        reminders.map { it.toMedicationEntity() }.forEach {
            dao.insertMedication(it)
        }
    }

    override suspend fun deleteMedication(reminder: Reminder) {
        dao.deleteMedication(reminder.toMedicationEntity())
    }

    override suspend fun updateMedication(reminder: Reminder) {
        dao.updateMedication(reminder.toMedicationEntity())
    }

    override fun getAllMedications(): Flow<List<Reminder>> {
        return dao.getAllMedications().map { entities ->
            entities.map { it.toMedication() }
        }
    }

    override fun getMedicationsForDate(date: String): Flow<List<Reminder>> {
        return dao.getMedicationsForDate(
            date = date
        ).map { entities ->
            entities.map { it.toMedication() }
        }
    }
}
