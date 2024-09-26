package com.example.doc_di.reminder.booked_reminder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.doc_di.domain.reminder.ReminderImpl
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.booked_reminder.utils.EditDepartment
import com.example.doc_di.reminder.booked_reminder.utils.EditDoctorName
import com.example.doc_di.reminder.booked_reminder.utils.EditEndDate
import com.example.doc_di.reminder.booked_reminder.utils.EditHospitalName
import com.example.doc_di.reminder.booked_reminder.utils.EditTimerTextField
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.util.Department
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    reminderId: Int?
){
    val reminderImpl = ReminderImpl(RetrofitInstance.reminderApi)
    val booked by remember(reminderId) { mutableStateOf(reminderId?.let { reminderViewModel.getBookedReminderById(it) }) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var clinicName by rememberSaveable { mutableStateOf("") }
    var doctorName by rememberSaveable { mutableStateOf("") }
    var isRecurring by rememberSaveable { mutableStateOf(false) }  // Add state for toggle
    var department by rememberSaveable { mutableStateOf(Department.InternalMedicine.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    var existingDate by remember { mutableStateOf(Date()) }
    var bookTime by rememberSaveable { mutableStateOf("") }

    var isClinicEntered by remember { mutableStateOf(true) }
    var isDoctorEntered by remember { mutableStateOf(true) }
    var isDepartmentSelected by remember { mutableStateOf(true) }
    var isTimeSelected by remember { mutableStateOf(true) }
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
    val isSaveButtonEnabled = isClinicEntered && isDoctorEntered && isDepartmentSelected && isTimeSelected

    LaunchedEffect(booked) {
        booked?.let {
            clinicName = it.hospitalName
            doctorName = it.doctorName
            department = it.subject
            bookTime = it.bookTime

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parts = it.bookTime.split(" ")
            existingDate = dateFormat.parse(parts[0]) ?: Date() // 기존 날짜 추출
        }
    }


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
                text = { Text("수정 완료",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White) },
                onClick = {
                    if (isSaveButtonEnabled) {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                        // 기존 날짜에서 Calendar 객체 생성
                        val existingCalendar = Calendar.getInstance().apply {
                            time = existingDate // 기존 날짜를 설정
                        }

                        val updatedTimes = selectedTimes.map { calendarInfo ->
                            val calendar = calendarInfo.getCalendar()
                            val hour = calendar.get(Calendar.HOUR_OF_DAY)
                            val minute = calendar.get(Calendar.MINUTE)

                            // 기존 날짜의 시간 부분을 새로운 시간으로 업데이트
                            existingCalendar.set(Calendar.HOUR_OF_DAY, hour)
                            existingCalendar.set(Calendar.MINUTE, minute)

                            // 최종적으로 포맷팅하여 문자열로 변환
                            dateFormat.format(existingCalendar.time)
                        }
                        val updatedReminder = booked?.copy(
                            hospitalName = clinicName,
                            doctorName = doctorName,
                            subject = department,
                            bookTime = updatedTimes.joinToString(", ")
                        )

                        updatedReminder?.let {
                            scope.launch {
                                reminderImpl.editBookedReminder(
                                    booked = it,
                                    context = context,
                                    isAllWritten = isSaveButtonEnabled,
                                    isAllAvailable = isSaveButtonEnabled,
                                    navController = navController
                                )
                            }
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
                shape = MaterialTheme.shapes.extraLarge
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
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hospitalemoji),
                    contentDescription = "Icon",
                    modifier = Modifier.size(30.dp) // Adjust size if necessary
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "진료 일정 수정",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                )
            }

            EditHospitalName(
                hospitalName = clinicName,
                isHospitalEntered = isClinicEntered,
                onHospitalChange = { hospital ->
                    clinicName = hospital
                    isClinicEntered = hospital.isNotEmpty()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            EditDoctorName(
                doctorName = doctorName,
                isDoctorEntered = isDoctorEntered,
                onDoctorChange = { doctor ->
                    doctorName = doctor
                    isDoctorEntered = doctor.isNotEmpty()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            EditDepartment(
                selectedDepartment = department,
                department = { selectedDepartment ->
                    department = selectedDepartment
                    isDepartmentSelected = true
                },
                isDepartmentSelected = isDepartmentSelected
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                color = Color.Black,
                text = "진료 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                EditTimerTextField(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                        setTimeSelected(index, true)
                    },
                    onDeleteClick = {
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                    isTimeSelected = true
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = Color.Black,
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
                EditEndDate (
                    onDateSelected = { selectedEndDate ->
                    endDate = selectedEndDate
                    isEndDateSelected = true
                },
                    isEndDateSelected = isEndDateSelected
            )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

