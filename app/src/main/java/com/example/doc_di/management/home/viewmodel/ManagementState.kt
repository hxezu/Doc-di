package com.example.doc_di.management.home.viewmodel

import com.example.doc_di.domain.model.Medication

data class ManagementState(
    val greeting: String = "",
    val userName: String = "",
    val lastSelectedDate: String = "",
    val medications: List<Medication> = emptyList()
)