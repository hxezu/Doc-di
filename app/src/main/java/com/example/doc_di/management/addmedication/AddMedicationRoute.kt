package com.example.doc_di.management.addmedication

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.management.addmedication.model.CalendarInformation
import com.example.doc_di.management.addmedication.viewmodel.AddMedicationViewModel
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.util.HOUR_MINUTE_FORMAT
import com.example.doc_di.util.Recurrence
import com.example.doc_di.util.SnackbarUtil.Companion.showSnackbar
import com.example.doc_di.util.getRecurrenceList
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Calendar
import java.util.Date


@Composable
fun AddMedicationRoute(
    onBackClicked: () -> Unit,
    navigateToMedicationConfirm: (List<Medication>) -> Unit,
    viewModel: AddMedicationViewModel = hiltViewModel()
) {
    AddMedicationScreen(onBackClicked, viewModel, navigateToMedicationConfirm)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onBackClicked: () -> Unit,
    viewModel: AddMedicationViewModel,
    navigateToMedicationConfirm: (List<Medication>) -> Unit,
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
                    .padding(vertical = 16.dp),
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
                RecurrenceDropdownMenu { recurrence = it }
            }

            if (isMaxDoseError) {
                Text(
                    text = "You cannot have more than 99 dosage per day.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            EndDateTextField { endDate = it }

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Time(s) for Medication",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                TimerTextField(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                    },
                    onDeleteClick = { removeTime(selectedTimes[index]) },
                    logEvent = {
                        viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                )
            }

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
    onValidate: (List<Medication>) -> Unit,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu(recurrence: (String) -> Unit) {
    val recurrenceMap = mapOf(
        Recurrence.Daily to "매일",
        Recurrence.Weekly to "매주",
        Recurrence.Monthly to "매달"
    )

    val options = getRecurrenceList().map { recurrenceMap[it] ?: "" }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "복용 주기",
            style = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                value = selectedOptionText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isFocused) MainBlue else Color.Black,
                    unfocusedBorderColor = if (isFocused) MainBlue else Color.Black
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                getRecurrenceList().forEach { recurrenceOption ->
                    DropdownMenuItem(
                        text = { Text(recurrenceMap[recurrenceOption] ?: "") },
                        onClick = {
                            selectedOptionText = recurrenceMap[recurrenceOption] ?: ""
                            recurrence(selectedOptionText)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun TimerTextField(
    isLastItem: Boolean,
    isOnlyItem: Boolean,
    time: (CalendarInformation) -> Unit,
    onDeleteClick: () -> Unit,
    logEvent: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    val currentTime = CalendarInformation(Calendar.getInstance())
    var selectedTime by rememberSaveable(
        stateSaver = CalendarInformation.getStateSaver()
    ) { mutableStateOf(currentTime) }

    // Show TimePickerDialogComponent when text field is pressed
    TimePickerDialogComponent(
        showDialog = isPressed,
        selectedDate = selectedTime,
        onSelectedTime = {
            logEvent.invoke()
            selectedTime = it
            time(it)
        }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = selectedTime.getDateFormatted(HOUR_MINUTE_FORMAT),
            onValueChange = {},
            trailingIcon = {
                if (isLastItem && !isOnlyItem) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = Color.Black
            ),
            interactionSource = interactionSource
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreenUI(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel
){
    var text by rememberSaveable { mutableStateOf("") }
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(
        CalendarInformation(
            Calendar.getInstance())
    ) }

    fun addTime(time: CalendarInformation) {
        selectedTimes.add(time)
    }
    fun removeTime(time: CalendarInformation) {
        selectedTimes.remove(time)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(70.dp)
                    .padding(vertical = 16.dp),
                navigationIcon = {
                    FloatingActionButton(
                        onClick = {
                        },
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MainBlue
                        )
                    }
                },
                title = {
                    Text(
                        text = "복용 약 추가",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // Apply paddingValues here to avoid overlapping with the TopAppBar
                .padding(horizontal = 20.dp)  // Additional padding as needed
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AddMedicationName()
            Spacer(modifier = Modifier.padding(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DoseInputField(maxDose = 99, onValueChange = { doseValue -> })
                RecurrenceDropdownMenu { recurrence = it }
            }

            Spacer(modifier = Modifier.padding(4.dp))
            EndDateTextField { endDate = it }

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "복용 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                TimerTextField(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                    },
                    onDeleteClick = {
                        removeTime(selectedTimes[index])
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        addTime(CalendarInformation(Calendar.getInstance()))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Text("시간 추가")
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                        .height(56.dp),
                    onClick = {
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Text(
                        text = "추가",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateTextField(endDate: (Long) -> Unit) {
    Text(
        text = "복용 종료일",
        style = MaterialTheme.typography.bodyLarge
    )

    var shouldDisplay by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        shouldDisplay = true
    }

    val today = Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)
    val currentDayMillis = today.timeInMillis
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= currentDayMillis
            }
        }
    )

    var selectedDate by rememberSaveable {
        mutableStateOf(
            datePickerState.selectedDateMillis?.toFormattedDateString() ?: ""
        )
    }

    EndDatePickerDialog(
        state = datePickerState,
        shouldDisplay = shouldDisplay,
        onConfirmClicked = { selectedDateInMillis ->
            selectedDate = selectedDateInMillis.toFormattedDateString()
            endDate(selectedDateInMillis)
        },
        dismissRequest = {
            shouldDisplay = false
        }
    )

    var isFocused by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = if (isFocused) MainBlue else Color.Black
            )
        },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isFocused) MainBlue else Color.Black,
            unfocusedBorderColor = if (isFocused) MainBlue else Color.Black,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        interactionSource = interactionSource
    )
}


@Composable
fun DoseInputField(
    maxDose: Int,
    onValueChange: (String) -> Unit,
    ) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isMaxDoseError by rememberSaveable { mutableStateOf(false) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Column {
        Text(
            text = "1회 투약량",
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = text,
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            onValueChange = {
                if (it.length <= maxDose) {
                    isMaxDoseError = false
                    text = it
                    onValueChange(it) // Pass the updated value to the parent composable
                } else {
                    isMaxDoseError = true
                }
            },
            trailingIcon = {
                if (isMaxDoseError) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = {
                Text(
                    "예시) 1",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            isError = isMaxDoseError,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isFocused) MainBlue else Color.Black,
                unfocusedBorderColor = if (isFocused) MainBlue else Color.Black
            ),
            singleLine = true,
            modifier = Modifier
                .width(128.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    //TO DO
                }
            )
        )
    }

    if (isMaxDoseError) {
        Text(
            text = "과다복용",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun AddMedicationName(){
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "제품명",
        style = MaterialTheme.typography.bodyLarge
    )

    OutlinedTextField(
        value = text,
        onValueChange = {text = it},
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "예시) 록프라정",
                color = Color.Black,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isFocused) MainBlue else Color.Black,
            unfocusedBorderColor = if (isFocused) MainBlue else Color.Black
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                //TO DO
            }
        )
        )

}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreenUIPreview( ){
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    AddMedicationScreenUI(navController = navController, btmBarViewModel = btmBarViewModel)
}