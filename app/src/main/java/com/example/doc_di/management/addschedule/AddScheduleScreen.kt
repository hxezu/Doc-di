package com.example.doc_di.management.addschedule

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.management.addmedication.model.CalendarInformation
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.util.Department
import com.example.doc_di.util.HOUR_MINUTE_FORMAT
import com.example.doc_di.util.Recurrence
import com.example.doc_di.util.getDepartmentList
import com.example.doc_di.util.getRecurrenceList
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreenUI(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel
){
    var isRecurring by rememberSaveable { mutableStateOf(false) }  // Add state for toggle
    var department by rememberSaveable { mutableStateOf(Department.InternalMedicine.name) }
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
                        text = "진료 일정 추가",
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
            AddHospitalName()
            Spacer(modifier = Modifier.padding(4.dp))

            AddDoctorName()
            Spacer(modifier = Modifier.padding(4.dp))

            DepartmentDropdownMenu { department = it }
            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "진료 시간",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "정기 진료",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.padding(end = 20.dp),
                    checked = isRecurring,
                    onCheckedChange = { checked -> isRecurring = checked },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MainBlue,
                        checkedTrackColor = MainBlue.copy(alpha = 0.5f)
                    )
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            if (isRecurring) {
                EndDateTextField { endDate = it }
                Spacer(modifier = Modifier.padding(4.dp))
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
fun DepartmentDropdownMenu(department: (String) -> Unit) {
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
            text = "진료 과목",
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateTextField(endDate: (Long) -> Unit) {
    Text(
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
fun AddDoctorName(){
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "의료진명",
        style = MaterialTheme.typography.bodyLarge
    )

    OutlinedTextField(
        value = text,
        onValueChange = {text = it},
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "예시) 조종호",
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

@Composable
fun AddHospitalName(){
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Text(
        text = "병원명",
        style = MaterialTheme.typography.bodyLarge
    )

    OutlinedTextField(
        value = text,
        onValueChange = {text = it},
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "예시) 강남세브란스병원",
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
fun AddScheduleScreenUIPreview( ){
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    AddScheduleScreenUI(navController = navController, btmBarViewModel = btmBarViewModel)
}