package com.example.doc_di.management.medicationdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.management.home.usecase.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    fun updateMedication(medication: Medication, isMedicationTaken: Boolean) {
        viewModelScope.launch {
            updateMedicationUseCase.updateMedication(medication.copy(medicationTaken = isMedicationTaken))
        }
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
}