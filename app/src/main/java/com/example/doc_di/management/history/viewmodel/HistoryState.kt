package com.example.doc_di.management.history.viewmodel

import com.example.doc_di.domain.model.Medication

data class HistoryState(
    val medications: List<Medication> = emptyList()
)