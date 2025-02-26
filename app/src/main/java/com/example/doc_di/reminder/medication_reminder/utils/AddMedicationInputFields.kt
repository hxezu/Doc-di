package com.example.doc_di.reminder.medication_reminder.utils

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.doc_di.R
import com.example.doc_di.etc.throttleFirst
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.reminder.util.Recurrence
import com.example.doc_di.reminder.util.getRecurrenceList
import com.example.doc_di.ui.theme.MainBlue
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu(
    recurrence: (String) -> Unit,
    isRecurrenceSelected: Boolean,
    onDisableEndDate: (Boolean) -> Unit
    ) {
    val recurrenceMap = mapOf(
        Recurrence.None to "선택 안함",
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
            color = Color.Black,
            text = "복용 주기",
            style = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                value = selectedOptionText,
                singleLine = true,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Black // 아이콘 색상 설정
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = if (isRecurrenceSelected) Color.Black else Color.Gray,
                    focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
                    unfocusedBorderColor = if (isRecurrenceSelected) MainBlue else Color.Gray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                getRecurrenceList().forEach { recurrenceOption ->
                    DropdownMenuItem(
                        text = { Text(recurrenceMap[recurrenceOption] ?: "") },
                        onClick = {{
                            selectedOptionText = recurrenceMap[recurrenceOption] ?: ""
                            recurrence(selectedOptionText)
                            expanded = false
                            onDisableEndDate(recurrenceOption == Recurrence.None)
                        }.throttleFirst()

                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseUnitDropdownMenu(
    doseUnit: (String) -> Unit,
    isDoseUnitSelected: Boolean
) {

    val doseUnitOptions = listOf("정", "캡슐", "ml", "mg", "g")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(doseUnitOptions[0]) } // Default selection
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            color = Color.Black,
            text = "복용 단위",
            style = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },

        ) {
            OutlinedTextField(
                value = selectedOptionText,
                singleLine = true,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Black // 아이콘 색상 설정
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = if (isDoseUnitSelected) Color.Black else Color.Gray,
                    focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
                    unfocusedBorderColor = if (isDoseUnitSelected) MainBlue else Color.Gray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                doseUnitOptions.forEach { unitOption ->
                    DropdownMenuItem(
                        text = { Text(unitOption) },
                        onClick = {{
                            selectedOptionText = unitOption
                            doseUnit(selectedOptionText) // Pass the selected option to the parent composable
                            expanded = false
                        }.throttleFirst()

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
    logEvent: () -> Unit,
    isTimeSelected: Boolean
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
            singleLine = true,
            value = selectedTime.getDateFormatted("a hh:mm"),
            onValueChange = {},
            trailingIcon = {
                if (isLastItem && !isOnlyItem) {
                    IconButton(
                        onClick = {
                            {
                                onDeleteClick
                                val nothing =""
                            }.throttleFirst()
                                  },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MainBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = if (isTimeSelected) MainBlue else Color.Gray,
                textColor = if (isTimeSelected) Color.Black else Color.Gray,
            ),
            interactionSource = interactionSource
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateTextField(
    startDate: Long,
    onDateSelected: (Long) -> Unit,
    isEndDateSelected: Boolean,
    isDisabled: Boolean
) {
    Text(
        color = Color.Black,
        text = "복용 종료일",
        style = MaterialTheme.typography.bodyLarge
    )

    var shouldDisplay by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        shouldDisplay = true
    }

    val context = LocalContext.current

    val startCalendar = Calendar.getInstance().apply {
        timeInMillis = startDate
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startDayMillis = startCalendar.timeInMillis

    var selectedDate by rememberSaveable {
        mutableStateOf("날짜를 선택해주세요")
    }

    if (shouldDisplay) {
        val datePickerDialog = remember {
            DatePickerDialog(
                context,
                R.style.CustomDatePickerTheme, // Apply your custom style here
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    val selectedDateInMillis = selectedCalendar.timeInMillis
                    selectedDate = selectedDateInMillis.toFormattedDateString()
                    onDateSelected(selectedDateInMillis)
                },
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                // Set the minimum selectable date
                datePicker.minDate = startDayMillis

                // Optional: Set maximum date if needed
                // datePicker.maxDate = someEndDateInMillis

                setOnDismissListener {
                    shouldDisplay = false
                }
            }
        }

        DisposableEffect(Unit) {
            datePickerDialog.show()
            onDispose {
                datePickerDialog.dismiss()
            }
        }
    }

    var isFocused by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        readOnly = true,
        singleLine = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = if (isFocused) MainBlue else Color.Gray
            )
        },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
            unfocusedBorderColor = if (isEndDateSelected) MainBlue else Color.Gray,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textColor = if (isEndDateSelected) Color.Black else Color.Gray,
        ),
        interactionSource = interactionSource,
        enabled = !isDisabled
    )
}



@Composable
fun DoseInputField(
    isDoseEntered: Boolean,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isInvalidInputError by rememberSaveable { mutableStateOf(false) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = "1회 복용량",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )


        OutlinedTextField(
            value = text,
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            onValueChange = {
                if (it.isEmpty()) {
                    isInvalidInputError = false
                    text = ""
                    onValueChange("")
                } else {
                    // Check if the input is a valid number
                    val numericValue = it.toIntOrNull()
                    if (numericValue != null) {
                            isInvalidInputError = false
                            text = it
                            onValueChange(it) // Pass the updated value to the parent composable

                    } else {
                        // Invalid input (not a number)
                        isInvalidInputError = true
                        text = ""
                    }
                }
            },
            trailingIcon = {
                if ( isInvalidInputError) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            placeholder = {
                Text(
                    text = "예시) 1",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            isError = isInvalidInputError,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
                unfocusedBorderColor = if (isDoseEntered) MainBlue else Color.Gray,
                cursorColor = MainBlue
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

        if (isInvalidInputError) {
            LaunchedEffect(isInvalidInputError) {
                delay(1000L) // Delay for 1 second
            }
            Text(
                text = "숫자만 입력 가능합니다.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun AddMedicationName(isNameEntered: Boolean,
                      onNameChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "제품명",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Black
    )

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onNameChange(it) // 부모 컴포저블에 텍스트 변경을 알림
        },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        placeholder = {
            Text(
                text = "예시) 록프라정",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
            unfocusedBorderColor = if (isNameEntered) MainBlue else Color.Gray,
            cursorColor = MainBlue
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