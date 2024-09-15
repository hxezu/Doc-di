package com.example.doc_di.domain.repository

import com.example.doc_di.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    suspend fun insertMedications(medications: List<Reminder>)

    suspend fun deleteMedication(medication: Reminder)

    suspend fun updateMedication(medication: Reminder)

    fun getAllMedications(): Flow<List<Reminder>>

    fun getMedicationsForDate(date: String): Flow<List<Reminder>>
}
