package com.example.doc_di.reminder.booked_reminder

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.reminder.ReminderImpl
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.booked_reminder.utils.AddDoctorName
import com.example.doc_di.reminder.booked_reminder.utils.AddHospitalName
import com.example.doc_di.reminder.booked_reminder.utils.AppointmentRecurrenceDropdownMenu
import com.example.doc_di.reminder.booked_reminder.utils.DepartmentDropdownMenu
import com.example.doc_di.reminder.booked_reminder.utils.EndDateTextField
import com.example.doc_di.reminder.booked_reminder.utils.TimerTextField
import com.example.doc_di.reminder.medication_reminder.model.CalendarInformation
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.reminder.util.Department
import com.example.doc_di.reminder.util.Recurrence
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
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

    var clinicName by rememberSaveable { mutableStateOf("") }
    var doctorName by rememberSaveable { mutableStateOf("") }
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.None.name) }
    var department by rememberSaveable { mutableStateOf(Department.InternalMedicine.name) }
    var endDate by rememberSaveable { mutableStateOf(Date()) }

    var isClinicEntered by remember { mutableStateOf(false) }
    var isDoctorEntered by remember { mutableStateOf(false) }
    var isDepartmentSelected by remember { mutableStateOf(false) }
    var isRecurrenceSelected by remember { mutableStateOf(false) }
    var isTimeSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }
    var isEndDateDisabled by remember { mutableStateOf(false) }

    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(CalendarInformation(Calendar.getInstance())) }
    var selectedTimeIndices by remember { mutableStateOf(setOf<Int>()) }
    var lastSelectedIndex by remember { mutableStateOf<Int?>(null) }

    val selectedDate = selectedDateString?.let { LocalDate.parse(it) }
    val bookDate = selectedDate?.let {
        Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
    } ?: Date()

    fun setTimeSelected(index: Int, isSelected: Boolean) {
        selectedTimeIndices = if (isSelected) {
            selectedTimeIndices + index
        } else {
            selectedTimeIndices - index
        }
        lastSelectedIndex = if (isSelected) index else lastSelectedIndex

        // Check if any time is selected
        isTimeSelected = selectedTimeIndices.isNotEmpty()
    }

    val isSaveButtonEnabled = isClinicEntered && isDoctorEntered && isDepartmentSelected && isRecurrenceSelected && isTimeSelected && (isEndDateSelected || isEndDateDisabled)
    val defaultDate = selectedDate?.let {
        Calendar.getInstance().apply {
            set(it.year, it.monthValue - 1, it.dayOfMonth, 23, 59, 59)
            set(Calendar.MILLISECOND, 999) // 밀리초를 999로 설정
        }.time
    } ?: Date()

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
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("저장",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White) },
                onClick = {
                    if (isSaveButtonEnabled) {
                        scope.launch {
                            reminderImpl.createBookedReminder(
                                email = userEmail,
                                hospitalName = clinicName,
                                doctorName = doctorName,
                                subject = department,
                                startDate = bookDate,
                                recurrence = recurrence,
                                endDate = endDate,
                                bookTimes = selectedTimes,
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
                    painter = painterResource(id = R.drawable.hospitalemoji),
                    contentDescription = "Icon",
                    modifier = Modifier.size(30.dp) // Adjust size if necessary
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "진료 일정 추가",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                )
            }

            AddHospitalName(
                isHospitalEntered = isClinicEntered,
                onHospitalChange = { hospital ->
                    clinicName = hospital
                    isClinicEntered = hospital.isNotEmpty()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            AddDoctorName(
                isDoctorEntered = isDoctorEntered,
                onDoctorChange = { doctor ->
                    doctorName = doctor
                    isDoctorEntered = doctor.isNotEmpty()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            DepartmentDropdownMenu(
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
                TimerTextField(
                    isLastItem = true,
                    isOnlyItem = true,
                    time = {
                        selectedTimes[index] = it
                        setTimeSelected(index, true)
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                    onDeleteClick = {
                        // 삭제 로직 필요할 경우 작성
                    },
                    isTimeSelected = selectedTimeIndices.contains(index)
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            AppointmentRecurrenceDropdownMenu (
                recurrence = { selectedRecurrence ->
                    recurrence = selectedRecurrence
                        isRecurrenceSelected = true
                             },
                    isRecurrenceSelected = isRecurrenceSelected,
                onDisableEndDate = { disableEndDate ->
                    isEndDateDisabled = disableEndDate
                    if (disableEndDate) {
                        endDate = defaultDate
                        isEndDateSelected = false
                    }
                }
                )
            Spacer(modifier = Modifier.padding(4.dp))

            EndDateTextField (
                    bookDate = bookDate.time,
                    onDateSelected = { selectedEndDate ->
                    endDate = Date(selectedEndDate)
                    isEndDateSelected = true
                },
                    isEndDateSelected = isEndDateSelected,
                    isDisabled = isEndDateDisabled
            )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
}