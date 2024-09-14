package com.example.doc_di.reminder.medicationconfirm.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.reminder.medicationconfirm.usecase.AddMedicationUseCase
import com.example.doc_di.notification.MedicationNotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationConfirmViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _isMedicationSaved = MutableSharedFlow<Unit>()
    val isMedicationSaved = _isMedicationSaved.asSharedFlow()

    fun addMedication(context: Context, state: MedicationConfirmState) {
        viewModelScope.launch {
            val medications = state.reminders
            val medicationAdded = addMedicationUseCase.addMedication(medications)

            for (medication in medications) {
                val service = MedicationNotificationService(context)
                service.scheduleNotification(
                    reminder = medication,
                    analyticsHelper = analyticsHelper
                )
            }

            _isMedicationSaved.emit(medicationAdded)
        }
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
}