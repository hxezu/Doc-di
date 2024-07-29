package com.example.doc_di.management.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.extension.toFormattedYearMonthDateString
import com.example.doc_di.management.home.model.CalendarModel
import com.example.doc_di.management.home.usecase.GetMedicationsUseCase
import com.example.doc_di.management.home.usecase.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val analyticsHelper: AnalyticsHelper

) : ViewModel() {

    var state by mutableStateOf(ManagementState())
        private set
    private var dateFilter = savedStateHandle.getStateFlow(
        DATE_FILTER_KEY,
        Date().toFormattedYearMonthDateString()
    ).onEach {
        state = state.copy(
            lastSelectedDate = it
        )
    }

    private val _selectedDate = MutableStateFlow(Date())
    private val _medications = getMedicationsUseCase.getMedications()

    val homeUiState = combine(_selectedDate, _medications) { selectedDate, medications ->
        val filteredMedications = medications.filter {
            it.medicationTime.toFormattedDateString() == selectedDate.toFormattedDateString()
        }.sortedBy { it.medicationTime }

        ManagementState(
            medications = filteredMedications
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ManagementState()
    )

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    init {
        loadMedications()
    }

    fun getUserName() {
        state = state.copy(userName = "Kathryn")
        // TODO: Get user name from DB
    }

    fun getGreeting() {
        state = state.copy(greeting = "Greeting")
        // TODO: Get greeting by checking system time
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadMedications() {
        viewModelScope.launch {
            dateFilter.flatMapLatest { selectedDate ->
                getMedicationsUseCase.getMedications(selectedDate).onEach { medicationList ->
                    state = state.copy(
                        medications = medicationList
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun selectDate(selectedDate: CalendarModel.DateModel) {
        savedStateHandle[DATE_FILTER_KEY] = selectedDate.date.toFormattedYearMonthDateString()
    }

    fun takeMedication(medication: Medication) {
        viewModelScope.launch {
            updateMedicationUseCase.updateMedication(medication)
        }
    }

    fun getUserPlan() {
        // TODO: Get user plan
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
    companion object {
        const val DATE_FILTER_KEY = "medication_date_filter"
    }
}