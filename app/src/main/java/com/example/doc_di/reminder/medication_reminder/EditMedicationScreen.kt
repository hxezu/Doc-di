package com.example.doc_di.reminder.medication_reminder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.extension.toDate
import com.example.doc_di.reminder.home.viewmodel.ReminderViewModel
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.reminder.medication_reminder.utils.EditDoseInput
import com.example.doc_di.reminder.medication_reminder.utils.EditEndDate
import com.example.doc_di.reminder.medication_reminder.utils.EditMedicationName
import com.example.doc_di.reminder.medication_reminder.utils.EditRecurrence
import com.example.doc_di.reminder.medication_reminder.utils.EditTimerText
import com.example.doc_di.reminder.medication_reminder.utils.EndDateTextField
import com.example.doc_di.reminder.medication_reminder.utils.RecurrenceDropdownMenu
import com.example.doc_di.reminder.medication_reminder.utils.TimerTextField
import com.example.doc_di.ui.theme.MainBlue
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    reminderId: Int?
) {
    val reminder by remember(reminderId) { mutableStateOf(reminderId?.let { reminderViewModel.getReminderById(it) }) }

    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf(0) }
    var recurrence by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf(Date()) }

    // 데이터 로드 후 상태 초기화
    LaunchedEffect(reminder) {
        reminder?.let {
            name = it.medicineName
            dose = it.dosage
            recurrence = it.recurrence
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            endDate = dateFormat.parse(it.endDate) ?: Date()
        }
    }

    var isNameEntered by remember { mutableStateOf(false) }
    var isDoseEntered by remember { mutableStateOf(false) }
    var isRecurrenceSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }

    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(CalendarInformation(Calendar.getInstance())) }
    var selectedTimeIndices by remember { mutableStateOf(setOf<Int>()) }
    var lastSelectedIndex by remember { mutableStateOf<Int?>(null) }
    fun setTimeSelected(index: Int, isSelected: Boolean) {
        selectedTimeIndices = if (isSelected) { selectedTimeIndices + index } else { selectedTimeIndices - index }
        lastSelectedIndex = if (isSelected) index else lastSelectedIndex
    }

    fun addTime(time: CalendarInformation) { selectedTimes.add(time) }

    fun removeTime(time: CalendarInformation) { selectedTimes.remove(time) }

    val isTimerButtonEnabled = selectedTimes.isNotEmpty() && selectedTimeIndices.contains(selectedTimes.lastIndex)
    val isSaveButtonEnabled = isNameEntered && isDoseEntered && isRecurrenceSelected && isEndDateSelected && selectedTimes.isNotEmpty()


    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.managementScreen.route) {
                                navController.popBackStack()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        "수정 완료",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                },
                onClick = {
                    if (isSaveButtonEnabled) {
//                        reminderViewModel.editReminder (
//                            medicineName = name,
//                            dosage = dose.toShort(),
//                            recurrence = recurrence,
//                            endDate = endDate,
//                            medicationTimes = selectedTimes
//                        )
                        navController.navigate(Routes.managementScreen.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                },
                containerColor = if (isSaveButtonEnabled) MainBlue else Color.Gray,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 0.dp,
                ),
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                shape = MaterialTheme.shapes.extraLarge,
            )

        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .padding(paddingValues)  // Apply paddingValues here to avoid overlapping with the TopAppBar
                .padding(horizontal = 20.dp)  // Additional padding as needed
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp),
                verticalAlignment = CenterVertically // Aligns the image and text vertically in the center
            ){
                Image(
                    painter = painterResource(id = R.drawable.pillemoji),
                    contentDescription = "Icon",
                    modifier = Modifier.size(30.dp) // Adjust size if necessary
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "복용 약 수정",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                )
            }

            EditMedicationName(
                medicationName = name,
                isNameEntered = true,
                onNameChange = { nameValue ->
                    name = nameValue
                    isNameEntered = nameValue.isNotEmpty()
                })
            Spacer(modifier = Modifier.padding(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                EditDoseInput(
                    dose = dose,
                    isDoseEntered = true,
                    maxDose = 99,
                    onValueChange = { doseValue ->
                        dose = doseValue
                        isDoseEntered = doseValue > 0
                    })
                EditRecurrence (
                    selectedRecurrence = recurrence,
                    recurrence = { selectedRecurrence ->
                        recurrence = selectedRecurrence
                        isRecurrenceSelected = true
                    },
                    isRecurrenceSelected = true)
            }

            Spacer(modifier = Modifier.padding(4.dp))
            EditEndDate(
                endDate = endDate,
                onDateSelected = { timestamp ->
                    endDate = Date(timestamp) // Convert the Long timestamp to a Date object
                    isEndDateSelected = true
                },
                isEndDateSelected = true
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                color = Color.Black,
                text = "복용 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                EditTimerText(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                        setTimeSelected(index, true)
                    },
                    onDeleteClick = {
                        removeTime(selectedTimes[index])
                        setTimeSelected(index, false)
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                    isTimeSelected = selectedTimeIndices.contains(index)
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.padding(bottom = 70.dp),
                    onClick = {
                        if (isTimerButtonEnabled) {
                            addTime(CalendarInformation(Calendar.getInstance()))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = if (isTimerButtonEnabled) MainBlue else Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = isTimerButtonEnabled
                ) {
                    Icon(imageVector = Icons.Default.Notifications,
                        contentDescription = "Add",
                        tint = if (isTimerButtonEnabled) MainBlue else Color.Gray)
                    Text("시간 추가", color = if (isTimerButtonEnabled) MainBlue else Color.Gray, modifier = Modifier.padding(start = 10.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}