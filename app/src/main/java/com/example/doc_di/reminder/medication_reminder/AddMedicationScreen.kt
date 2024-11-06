package com.example.doc_di.reminder.medication_reminder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.reminder.ReminderImpl
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.reminder.medication_reminder.utils.AddMedicationName
import com.example.doc_di.reminder.medication_reminder.utils.DoseInputField
import com.example.doc_di.reminder.medication_reminder.utils.DoseUnitDropdownMenu
import com.example.doc_di.reminder.medication_reminder.utils.EndDateTextField
import com.example.doc_di.reminder.medication_reminder.utils.RecurrenceDropdownMenu
import com.example.doc_di.reminder.medication_reminder.utils.TimerTextField
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.reminder.util.Recurrence
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreenUI(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    selectedDateString: String?
){
    val reminderImpl = ReminderImpl(RetrofitInstance.reminderApi)
    val userEmail = userViewModel.userInfo.value?.email ?: ""
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var dose by rememberSaveable { mutableStateOf("") }
    var doseUnit by rememberSaveable { mutableStateOf("") }
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableStateOf(Date()) }
    var medicationTaken by rememberSaveable { mutableStateOf("") }

    var isNameEntered by remember { mutableStateOf(false) }
    var isDoseEntered by remember { mutableStateOf(false) }
    var isDoseUnitSelected by remember { mutableStateOf(false) }
    var isRecurrenceSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }
    var isEndDateDisabled by remember { mutableStateOf(false) }

    val selectedDate = selectedDateString?.let { LocalDate.parse(it) }
    val startDate = selectedDate?.let {
        Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
    } ?: Date()

    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(CalendarInformation(Calendar.getInstance())) }
    var selectedTimeIndices by remember { mutableStateOf(setOf<Int>()) }
    var lastSelectedIndex by remember { mutableStateOf<Int?>(null) }

    fun setTimeSelected(index: Int, isSelected: Boolean) {
        selectedTimeIndices = if (isSelected) {
            selectedTimeIndices + index
        } else {
            selectedTimeIndices - index
        }
        lastSelectedIndex = if (isSelected) index else lastSelectedIndex
    }

    fun addTime(time: CalendarInformation) { selectedTimes.add(time) }

    fun removeTime(time: CalendarInformation) { selectedTimes.remove(time) }

    // Check if the last TimerTextField is selected
    val isTimerButtonEnabled = selectedTimes.isNotEmpty() && selectedTimeIndices.contains(selectedTimes.lastIndex)
    val isSaveButtonEnabled = isNameEntered && isDoseEntered && isDoseUnitSelected && isRecurrenceSelected && selectedTimes.isNotEmpty() &&
            (isEndDateSelected || isEndDateDisabled)


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
                            navController.popBackStack()
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
                text = { Text("저장",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White )},
                onClick = {
                    if (isSaveButtonEnabled) {
                        scope.launch {
                            val dosageString = "$dose $doseUnit"
                            val groupId = generateGroupId(userEmail, System.currentTimeMillis())

                            reminderImpl.createReminder(
                                email = userEmail,
                                medicineName = name,
                                dosage = dosageString,
                                recurrence = recurrence,
                                startDate = startDate,
                                endDate = endDate,
                                medicationTimes = selectedTimes,
                                medicationTaken = groupId,
                                context = context,
                                isAllWritten  = isSaveButtonEnabled,
                                isAllAvailable  = isSaveButtonEnabled,
                                navController = navController
                            )
                        }
                        navController.navigate(Routes.managementScreen.route)
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Default.Check,
                        contentDescription = "Add",
                        tint = Color.White)
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
                    text = "복용 약 추가",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                )
            }

            AddMedicationName(
                isNameEntered = isNameEntered,
                onNameChange = { nameValue ->
                    name = nameValue
                    isNameEntered = nameValue.isNotEmpty()
                })
            Spacer(modifier = Modifier.padding(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DoseInputField(
                    isDoseEntered = isDoseEntered,
                    onValueChange = { doseValue ->
                        dose = doseValue
                        isDoseEntered = doseValue.isNotEmpty()
                    })
                DoseUnitDropdownMenu(
                    isDoseUnitSelected = isDoseUnitSelected,
                    doseUnit = { selectedDoseUnit ->
                        doseUnit = selectedDoseUnit
                        isDoseUnitSelected = true
                    })

            }

            Spacer(modifier = Modifier.padding(4.dp))

            RecurrenceDropdownMenu(
                recurrence = { selectedRecurrence ->
                    recurrence = selectedRecurrence
                    isRecurrenceSelected = true
                },
                isRecurrenceSelected = isRecurrenceSelected,
                onDisableEndDate = { disableEndDate ->
                    isEndDateDisabled = disableEndDate
                    if (disableEndDate) {
                        endDate = startDate
                        isEndDateSelected = false
                    }
                }
            )

            Spacer(modifier = Modifier.padding(4.dp))
            EndDateTextField(
                startDate = startDate.time,
                onDateSelected = { timestamp ->
                    endDate = Date(timestamp)
                    isEndDateSelected = true
                },
                isEndDateSelected = isEndDateSelected,
                isDisabled = isEndDateDisabled
            )

                Spacer(modifier = Modifier.padding(4.dp))

            Text(
                color = Color.Black,
                text = "복용 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                TimerTextField(
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


                            scope.launch {
                                val maxScroll = scrollState.maxValue + 150 // 버튼 크기만큼 더 스크롤
                                scrollState.animateScrollTo(maxScroll)
                            }
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

private fun generateGroupId(email: String, registrationTime: Long): String {
    val input = "$email-$registrationTime"
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(input.toByteArray())
    return hash.joinToString("") { "%02x".format(it) }
}
