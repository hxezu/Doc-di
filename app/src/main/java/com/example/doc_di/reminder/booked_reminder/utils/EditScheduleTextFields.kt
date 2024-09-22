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
import com.example.doc_di.util.Department
import com.example.doc_di.util.getDepartmentList
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDepartment(
    selectedDepartment: String,
    department: (String) -> Unit,
    isDepartmentSelected: Boolean
) {
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
    var selectedOptionText by remember { mutableStateOf(selectedDepartment) }
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
                value = selectedDepartment,
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
fun EditTimerTextField(
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
    var selectedTime by rememberSaveable(stateSaver = CalendarInformation.getStateSaver()) { mutableStateOf(currentTime) }


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
            value =  selectedTime.getDateFormatted("a HH:mm"),
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
fun EditEndDate(onDateSelected: (Long) -> Unit,
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
fun EditDoctorName(
    doctorName: String,
    isDoctorEntered: Boolean,
    onDoctorChange: (String) -> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "의료진명",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Black
    )

    OutlinedTextField(
        value = doctorName,
        onValueChange = {
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
            unfocusedBorderColor = if (isDoctorEntered) MainBlue else Color.Gray
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
fun EditHospitalName(
    hospitalName: String,
    isHospitalEntered: Boolean,
    onHospitalChange: (String) -> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "병원명",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Black
    )

    OutlinedTextField(
        value = hospitalName,
        onValueChange = {
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
            unfocusedBorderColor = if (isHospitalEntered) MainBlue else Color.Gray
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