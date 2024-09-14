package com.example.doc_di.reminder.addmedication

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.reminder.addmedication.model.CalendarInformation
import com.example.doc_di.reminder.addmedication.viewmodel.AddMedicationViewModel
import com.example.doc_di.util.Recurrence
import com.example.doc_di.util.SnackbarUtil.Companion.showSnackbar
import java.util.Calendar
import java.util.Date


@Composable
fun AddMedicationRoute(
    onBackClicked: () -> Unit,
    navigateToMedicationConfirm: (List<Reminder>) -> Unit,
    viewModel: AddMedicationViewModel = hiltViewModel()
) {
    AddMedicationScreen(onBackClicked, viewModel, navigateToMedicationConfirm)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onBackClicked: () -> Unit,
    viewModel: AddMedicationViewModel,
    navigateToMedicationConfirm: (List<Reminder>) -> Unit,
) {
    var medicationName by rememberSaveable { mutableStateOf("") }
    var numberOfDosage by rememberSaveable { mutableStateOf("1") }
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(
        CalendarInformation(
        Calendar.getInstance())
    ) }
    val context = LocalContext.current

    fun addTime(time: CalendarInformation) {
        selectedTimes.add(time)
        viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_ADD_TIME_CLICKED)
    }

    fun removeTime(time: CalendarInformation) {
        selectedTimes.remove(time)
        viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_DELETE_TIME_CLICKED)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(vertical = 25.dp),
                navigationIcon = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_ON_BACK_CLICKED)
                            onBackClicked()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Add Medication",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                onClick = {
                    validateMedication(
                        name = medicationName,
                        dosage = numberOfDosage.toIntOrNull() ?: 0,
                        recurrence = recurrence,
                        endDate = endDate,
                        selectedTimes = selectedTimes,
                        onInvalidate = {
                            val invalidatedValue = context.getString(it)
                            showSnackbar(
                                context.getString(
                                    R.string.value_is_empty,
                                    invalidatedValue
                                )
                            )

                            val event = String.format(
                                AnalyticsEvents.ADD_MEDICATION_MEDICATION_VALUE_INVALIDATED,
                                invalidatedValue
                            )
                            viewModel.logEvent(eventName = event)
                        },
                        onValidate = {
                            navigateToMedicationConfirm(it)
                            viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_NAVIGATING_TO_MEDICATION_CONFIRM)
                        },
                        viewModel = viewModel
                    )
                },
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Medication Name",
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = medicationName,
                onValueChange = { medicationName = it },
                placeholder = {
                    Text(
                        text = "e.g. Risperdal, 4mg"
                    )
                },
            )

            Spacer(modifier = Modifier.padding(4.dp))

            var isMaxDoseError by rememberSaveable { mutableStateOf(false) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val maxDose = 3

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Dosage",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    TextField(
                        modifier = Modifier.width(128.dp),
                        value = numberOfDosage,
                        onValueChange = {
                            if (it.length < maxDose) {
                                isMaxDoseError = false
                                numberOfDosage = it
                            } else {
                                isMaxDoseError = true
                            }
                        },
                        trailingIcon = {
                            if (isMaxDoseError) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Erroe",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        placeholder = {
                            Text(
                                text = "e.g. 1"
                            )
                        },
                        isError = isMaxDoseError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
//                RecurrenceDropdownMenu(
//                    recurrence = { selectedRecurrence ->
//                        recurrence = selectedRecurrence
//                        isRecurrenceSelected = true
//                    },
//                    isRecurrenceSelected = isRecurrenceSelected
//                )
            }

            if (isMaxDoseError) {
                Text(
                    text = "You cannot have more than 99 dosage per day.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            //EndDateTextField { endDate = it }

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Time(s) for Medication",
                style = MaterialTheme.typography.bodyLarge
            )

//            for (index in selectedTimes.indices) {
//                TimerTextField(
//                    isLastItem = selectedTimes.lastIndex == index,
//                    isOnlyItem = selectedTimes.size == 1,
//                    time = {
//                        selectedTimes[index] = it
//                    },
//                    onDeleteClick = { removeTime(selectedTimes[index]) },
//                    logEvent = {
//                        viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
//                    },
//                )
//            }

            Button(
                onClick = { addTime(CalendarInformation(Calendar.getInstance())) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Text("Add Time")
            }
        }
    }
}

private fun validateMedication(
    name: String,
    dosage: Int,
    recurrence: String,
    endDate: Long,
    selectedTimes: List<CalendarInformation>,
    onInvalidate: (Int) -> Unit,
    onValidate: (List<Reminder>) -> Unit,
    viewModel: AddMedicationViewModel
) {
    if (name.isEmpty()) {
        onInvalidate(R.string.medication_name)
        return
    }

    if (dosage < 1) {
        onInvalidate(R.string.dose_per_day)
        return
    }

    if (endDate < 1) {
        onInvalidate(R.string.end_date)
        return
    }

    if (selectedTimes.isEmpty()) {
        onInvalidate(R.string.times_for_medication)
        return
    }

    val medications =
        viewModel.createMedications(name, dosage, recurrence, Date(endDate), selectedTimes)

    onValidate(medications)
}

private fun handleSelection(
    isSelected: Boolean,
    selectionCount: Int,
    canSelectMoreTimesOfDay: Boolean,
    onStateChange: (Int, Boolean) -> Unit,
    onShowMaxSelectionError: () -> Unit
) {
    if (isSelected) {
        onStateChange(selectionCount - 1, !isSelected)
    } else {
        if (canSelectMoreTimesOfDay) {
            onStateChange(selectionCount + 1, !isSelected)
        } else {
            onShowMaxSelectionError()
        }
    }
}

private fun canSelectMoreTimesOfDay(selectionCount: Int, numberOfDosage: Int): Boolean {
    return selectionCount < numberOfDosage
}

private fun showMaxSelectionSnackbar(
    numberOfDosage: String,
    context: Context
) {
    val dosage = ((numberOfDosage.toIntOrNull() ?: 0) + 1).toString()
    showSnackbar(
        context.getString(
            R.string.dosage_and_frequency_mismatch_error_message,
            dosage
        )
    )
}




