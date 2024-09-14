package com.example.doc_di.reminder.medicationdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.reminder.home.usecase.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    fun updateMedication(reminder: Reminder, isMedicationTaken: Boolean) {
        viewModelScope.launch {
            updateMedicationUseCase.updateMedication(reminder.copy(medicationTaken = isMedicationTaken))
        }
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
}