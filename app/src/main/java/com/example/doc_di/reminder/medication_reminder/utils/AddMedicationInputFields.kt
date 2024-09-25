package com.example.doc_di.reminder.medication_reminder.utils

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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.util.Recurrence
import com.example.doc_di.util.getRecurrenceList
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu(recurrence: (String) -> Unit,
                           isRecurrenceSelected: Boolean) {
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
            value = selectedTime.getDateFormatted("a HH:mm"),
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
    isEndDateSelected: Boolean
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

    val startCalendar = Calendar.getInstance().apply {
        timeInMillis = startDate
        add(Calendar.DAY_OF_MONTH,0)
    }
    startCalendar.set(Calendar.HOUR_OF_DAY, 0)
    startCalendar.set(Calendar.MINUTE, 0)
    startCalendar.set(Calendar.SECOND, 0)
    startCalendar.set(Calendar.MILLISECOND, 0)
    val startDayMillis = startCalendar.timeInMillis
    println("startDayMillis: $startDayMillis")

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDayMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= startDayMillis // StartDate 이후의 날짜만 선택 가능
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
            onDateSelected(selectedDateInMillis)
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
        interactionSource = interactionSource
    )
}


@Composable
fun DoseInputField(
    isDoseEntered: Boolean,
    maxDose: Int,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isMaxDoseError by rememberSaveable { mutableStateOf(false) }
    var isInvalidInputError by rememberSaveable { mutableStateOf(false) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = "1회 투약량",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )


        OutlinedTextField(
            value = text,
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            onValueChange = {
                if (it.isEmpty()) {
                    isInvalidInputError = false
                    isMaxDoseError = false
                    text = ""
                    onValueChange("")
                } else {
                    // Check if the input is a valid number
                    val numericValue = it.toIntOrNull()
                    if (numericValue != null) {
                        if (numericValue <= maxDose) {
                            isMaxDoseError = false
                            isInvalidInputError = false
                            text = it
                            onValueChange(it) // Pass the updated value to the parent composable
                        } else {
                            isMaxDoseError = true
                            text = ""
                        }
                    } else {
                        // Invalid input (not a number)
                        isInvalidInputError = true
                        text = ""
                    }
                }
            },
            trailingIcon = {
                if (isMaxDoseError || isInvalidInputError) {
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
            isError = isMaxDoseError || isInvalidInputError,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
                unfocusedBorderColor = if (isDoseEntered) MainBlue else Color.Gray
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
        if (isMaxDoseError) {
            LaunchedEffect(isMaxDoseError) {
                delay(1000L) // Delay for 1 second
                isMaxDoseError = false // Reset the error state
            }
            Text(
                text = "과다복용",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
    }
        if (isInvalidInputError) {
            LaunchedEffect(isInvalidInputError) {
                delay(1000L) // Delay for 1 second
                isMaxDoseError = false // Reset the error state
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
                      onNameChange: (String) -> Unit){
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
            unfocusedBorderColor = if (isNameEntered) MainBlue else Color.Gray
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