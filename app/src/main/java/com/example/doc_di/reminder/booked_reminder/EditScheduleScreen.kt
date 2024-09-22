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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.model.Booked
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.domain.reminder.ReminderImpl
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.booked_reminder.utils.AddDoctorName
import com.example.doc_di.reminder.booked_reminder.utils.AddHospitalName
import com.example.doc_di.reminder.booked_reminder.utils.DepartmentDropdownMenu
import com.example.doc_di.reminder.booked_reminder.utils.EndDateTextField
import com.example.doc_di.reminder.booked_reminder.utils.TimerTextField
import com.example.doc_di.reminder.home.viewmodel.ReminderViewModel
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
    var bookTime by rememberSaveable { mutableStateOf("") }

    var isClinicEntered by remember { mutableStateOf(false) }
    var isDoctorEntered by remember { mutableStateOf(false) }
    var isDepartmentSelected by remember { mutableStateOf(false) }
    var isTimeSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }

    var selectedTimeValue by rememberSaveable { mutableStateOf("") }
    val isSaveButtonEnabled = isClinicEntered && isDoctorEntered && isDepartmentSelected && isTimeSelected

    LaunchedEffect(booked) {
        booked?.let {
            clinicName = it.hospitalName
            doctorName = it.doctorName
            department = it.subject
            bookTime = it.bookTime

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
                text = { Text("수정 완료",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White) },
                onClick = {
                    if (isSaveButtonEnabled) {
                        val existingDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val existingDate: Date? = existingDateFormat.parse(bookTime)

                        val calendar = Calendar.getInstance().apply {
                            existingDate?.let {
                                time = it // Set existing date

                                val selectedTimeParts = selectedTimeValue.split(":")
                                if (selectedTimeParts.size == 2) {
                                    set(Calendar.HOUR_OF_DAY, selectedTimeParts[0].toInt())
                                    set(Calendar.MINUTE, selectedTimeParts[1].toInt())
                                }
                            }
                        }
                        val updatedReminder = Booked(
                            id = reminderId!!, // assuming booked has the existing ID
                            hospitalName = clinicName,
                            doctorName = doctorName,
                            subject = department,
                            bookTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time),
                        )

                        scope.launch {
                            reminderImpl.editBookedReminder(
                                booked = updatedReminder,
                                context = context,
                                isAllWritten = isSaveButtonEnabled,
                                isAllAvailable = isSaveButtonEnabled,
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

            TimerTextField(
                isLastItem = true,
                isOnlyItem = true,
                time = { selectedTimeValue ->
                    //selectedTime = selectedTimeValue
                    isTimeSelected = true
                },
                logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                onDeleteClick = {
                    // 삭제 로직 필요할 경우 작성
                },
                isTimeSelected = isTimeSelected
                )

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
                EndDateTextField (
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

