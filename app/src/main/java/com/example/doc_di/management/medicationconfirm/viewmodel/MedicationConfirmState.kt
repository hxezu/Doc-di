package com.example.doc_di.management.medicationconfirm.viewmodel

import com.example.doc_di.domain.model.Medication

data class MedicationConfirmState(
    val medications: List<Medication>
)