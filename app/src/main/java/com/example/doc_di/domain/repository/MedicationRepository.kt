package com.example.doc_di.domain.repository

import com.example.doc_di.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    suspend fun insertMedications(reminders: List<Reminder>)

    suspend fun deleteMedication(reminder: Reminder)

    suspend fun updateMedication(reminder: Reminder)

    fun getAllMedications(): Flow<List<Reminder>>

    fun getMedicationsForDate(date: String): Flow<List<Reminder>>
}
