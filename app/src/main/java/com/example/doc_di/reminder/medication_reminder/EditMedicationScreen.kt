package com.example.doc_di.reminder.medication_reminder

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.reminder.ReminderImpl
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.reminder.medication_reminder.utils.EditDoseInput
import com.example.doc_di.reminder.medication_reminder.utils.EditDoseUnit
import com.example.doc_di.reminder.medication_reminder.utils.EditEndDate
import com.example.doc_di.reminder.medication_reminder.utils.EditMedicationName
import com.example.doc_di.reminder.medication_reminder.utils.EditRecurrence
import com.example.doc_di.reminder.medication_reminder.utils.EditTimerText
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
    userViewModel: UserViewModel,
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    reminderId: Int?
) {

    val reminderImpl = ReminderImpl(RetrofitInstance.reminderApi)
    val reminder by remember(reminderId) { mutableStateOf(reminderId?.let { reminderViewModel.getReminderById(it) }) }
    var groupReminders by remember { mutableStateOf<List<Reminder>?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val userEmail = userViewModel.userInfo.value?.email ?: ""

    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf(0) }
    var doseUnit by rememberSaveable { mutableStateOf("") }
    var recurrence by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf(Date()) }
    var startDate by remember { mutableStateOf(Date()) }
    var isEndDateDisabled by remember { mutableStateOf(false) }
    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf<CalendarInformation>() }

    var isModified by remember { mutableStateOf(false) }

    LaunchedEffect(reminder) {
        reminder?.let { selectedReminder ->
            name = selectedReminder.medicineName
            dose = selectedReminder.dosage.split(" ")[0].toIntOrNull() ?: 0
            doseUnit = selectedReminder.dosage.split(" ").drop(1).joinToString(" ")
            recurrence = selectedReminder.recurrence
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedReminder.endDate) ?: Date()

            // 해당 그룹의 리마인더 데이터 로드
            groupReminders = selectedReminder.medicationTaken?.let { groupId ->
                reminderViewModel.getRemindersByGroupId(groupId)
            }

            // 그룹의 가장 빠른 날짜를 startDate로 설정
            startDate = groupReminders?.minOfOrNull { reminder ->
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(reminder.medicationTime) ?: Date()
            } ?: Date()

            selectedTimes.clear()

            val uniqueTimes = groupReminders
                ?.mapNotNull { reminder ->
                    val timePart = reminder.medicationTime.split(" ").getOrNull(1)
                    Log.d("EditMedicationScreen", "Extracted timePart: $timePart from ${reminder.medicationTime}")
                    timePart
                }
                ?.distinct() // 중복 시간 제거
                ?.sorted()   // 정렬
                ?: emptyList()

            uniqueTimes.forEach { timePart ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val calendar = Calendar.getInstance().apply {
                    time = timeFormat.parse(timePart) ?: Date()
                }
                Log.d("EditMedicationScreen", "Adding to selectedTimes: $timePart as CalendarInformation(${calendar.time})")
                selectedTimes.add(CalendarInformation(calendar))
            }


            isEndDateDisabled = (recurrence == "선택 안함")
            isModified = false
        }
    }

    fun markModified() {
        isModified = true
    }


    fun updateGroupReminders() {
        if (isModified) {
            scope.launch {
                // 기존 그룹 리마인더 삭제
                groupReminders?.forEach { reminder ->
                    reminderViewModel.deleteReminder(reminder.id!!)
                }

                // 업데이트된 리마인더 그룹 생성
                reminderImpl.createReminder(
                    email = userEmail,
                    medicineName = name,
                    dosage = "$dose $doseUnit",
                    recurrence = recurrence,
                    startDate = startDate,
                    endDate = endDate,
                    medicationTimes = selectedTimes,
                    medicationTaken = reminder?.medicationTaken ?: "",
                    context = context,
                    isAllWritten = true,
                    isAllAvailable = true,
                    navController = navController
                )
                navController.navigate(Routes.managementScreen.route)
            }
        }
    }


    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("수정 완료", color = Color.White) },
                onClick = { updateGroupReminders() },
                icon = { Icon(imageVector = Icons.Default.Check, contentDescription = "Save", tint = Color.White) },
                containerColor = if (isModified) MainBlue else Color.Gray,
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 10.dp, pressedElevation = 0.dp)
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp),
                verticalAlignment = CenterVertically // Aligns the image and text vertically in the center
            ) {
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
                isNameEntered = name.isNotEmpty(),
                onNameChange = {
                    name = it
                    markModified() // Mark as modified
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                EditDoseInput(
                    dose = dose,
                    isDoseEntered = dose > 0,
                    onValueChange = {
                        dose = it
                        markModified() // Mark as modified
                    }
                )
                EditDoseUnit(
                    selectedDoseUnit = doseUnit,
                    isDoseUnitSelected = doseUnit.isNotEmpty(),
                    doseUnit = { unit ->
                        doseUnit = unit
                        markModified() // Mark as modified
                    }
                )

            }

            Spacer(modifier = Modifier.padding(4.dp))

            EditRecurrence(
                selectedRecurrence = recurrence,
                recurrence = { rec ->
                    recurrence = rec
                    markModified() // Mark as modified
                },
                isRecurrenceSelected = recurrence.isNotEmpty(),
                onDisableEndDate = { disableEndDate ->
                    isEndDateDisabled = disableEndDate
                    if (disableEndDate) {
                        endDate = startDate
                    }
                    markModified()
                }
            )


            Spacer(modifier = Modifier.padding(4.dp))
            EditEndDate(
                endDate = endDate,
                onDateSelected = { timestamp ->
                    endDate = Date(timestamp)
                    markModified()
                },
                isEndDateSelected = true,
                isDisabled = isEndDateDisabled
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                color = Color.Black,
                text = "복용 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                println("selectedTimes : $selectedTimes")
                EditTimerText(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    selectedTimes = selectedTimes,
                    time = {
                        selectedTimes[index] = it
                        markModified()
                    },
                    onDeleteClick = {
                        selectedTimes.removeAt(index)
                        markModified()
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                    isTimeSelected = true
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
                        selectedTimes.add(CalendarInformation(Calendar.getInstance()))
                        markModified()
                        scope.launch {
                            scrollState.animateScrollTo(scrollState.maxValue + 150)
                        }
                    },
                    enabled = selectedTimes.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = if (selectedTimes.isNotEmpty()) MainBlue else Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Add", tint = if (selectedTimes.isNotEmpty()) MainBlue else Color.Gray)
                    Text("시간 추가", color = if (selectedTimes.isNotEmpty()) MainBlue else Color.Gray, modifier = Modifier.padding(start = 10.dp))                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
