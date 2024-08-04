package com.example.doc_di.domain.repository

import com.example.doc_di.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {

    suspend fun insertMedications(medications: List<Medication>)

    suspend fun deleteMedication(medication: Medication)

    suspend fun updateMedication(medication: Medication)

    fun getAllMedications(): Flow<List<Medication>>

    fun getMedicationsForDate(date: String): Flow<List<Medication>>
}
