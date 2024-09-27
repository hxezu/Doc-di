package com.example.doc_di.reminder.booked_reminder.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
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
import com.example.doc_di.util.Department
import com.example.doc_di.util.Recurrence
import com.example.doc_di.util.getDepartmentList
import com.example.doc_di.util.getRecurrenceList
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentDropdownMenu(department: (String) -> Unit,
                           isDepartmentSelected: Boolean) {
    val departmentMap = mapOf(
        Department.InternalMedicine to "내과",
        Department.Surgery to "외과",
        Department.Otolaryngology to "이비인후과",
        Department.Ophthalmology to "안과",
        Department.DentalClinic to "치과",
        Department.Orthopedics to "정형외과",
        Department.PlasticSurgery to "성형외과",
        Department.Dermatology to "피부과",
        Department.ObstetricsAndGynecology to "산부인과",
        Department.Pediatrics to "소아과",
        Department.Psychiatry to "정신과",
        Department.OrientalMedicalClinic to "한의원",
    )

    val options = getDepartmentList().map { departmentMap[it] ?: "" }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            color = Color.Black,
            text = "진료 과목",
            style = MaterialTheme.typography.bodyLarge
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                singleLine = true,
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
                    textColor = if (isDepartmentSelected) Color.Black else Color.Gray,
                    focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
                    unfocusedBorderColor = if (isDepartmentSelected) MainBlue else Color.Gray
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                getDepartmentList().forEach { departmentOption ->
                    DropdownMenuItem(
                        text = { Text(departmentMap[departmentOption] ?: "") },
                        onClick = {
                            selectedOptionText = departmentMap[departmentOption] ?: ""
                            department(selectedOptionText)
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
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = selectedTime.getDateFormatted("a hh:mm"),
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
    bookDate: Long,
    onDateSelected: (Long) -> Unit,
    isEndDateSelected: Boolean) {
    Text(
        color = Color.Black,
        text = "정기 진료 종료일",
        style = MaterialTheme.typography.bodyLarge
    )

    var shouldDisplay by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        shouldDisplay = true
    }

    val startCalendar = Calendar.getInstance().apply {
        timeInMillis = bookDate
        add(Calendar.DAY_OF_MONTH, 0)
    }
    startCalendar.set(Calendar.HOUR_OF_DAY, 0)
    startCalendar.set(Calendar.MINUTE, 0)
    startCalendar.set(Calendar.SECOND, 0)
    startCalendar.set(Calendar.MILLISECOND, 0)
    val startDayMillis = startCalendar.timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDayMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= startDayMillis
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
            .padding(bottom = 70.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        singleLine = true,
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = if (isEndDateSelected) MainBlue else Color.Gray
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
fun AddDoctorName(isDoctorEntered: Boolean,
                  onDoctorChange: (String) -> Unit){
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "의료진명",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Black
    )

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onDoctorChange(it)
                        },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        placeholder = {
            Text(
                "예시) 조종호",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
            unfocusedBorderColor = if (isDoctorEntered) MainBlue else Color.Gray,
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

@Composable
fun AddHospitalName(isHospitalEntered: Boolean,
                    onHospitalChange: (String) -> Unit){
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "병원명",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Black
    )

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onHospitalChange(it)},
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        placeholder = {
            Text(
                "예시) 강남세브란스병원",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isFocused) MainBlue else Color.Gray,
            unfocusedBorderColor = if (isHospitalEntered) MainBlue else Color.Gray,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentRecurrenceDropdownMenu(recurrence: (String) -> Unit,
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
                singleLine = true,
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