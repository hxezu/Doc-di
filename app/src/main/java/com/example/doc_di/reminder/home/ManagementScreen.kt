package com.example.doc_di.reminder.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.UserViewModel
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.reminder.home.model.CalendarModel
import com.example.doc_di.reminder.home.utils.DatesHeader
import com.example.doc_di.reminder.home.viewmodel.ReminderState
import com.example.doc_di.reminder.home.utils.FabIcon
import com.example.doc_di.reminder.home.utils.FabOption
import com.example.doc_di.reminder.home.utils.MedicationCard
import com.example.doc_di.reminder.home.utils.MultiFabItem
import com.example.doc_di.reminder.home.utils.MultiFloatingActionButton
import com.example.doc_di.reminder.home.viewmodel.ReminderViewModel
import com.example.doc_di.ui.theme.MainBlue
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManagementScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    userViewModel: UserViewModel
) {
    val reminders by reminderViewModel.reminders
    val userEmail = userViewModel.userInfo.value?.email ?: ""

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(Unit) {
        if (userEmail.isNotEmpty()) {
            println("Fetching reminders for user: $userEmail")
            reminderViewModel.getReminders(userEmail) // Fetch reminders for the logged-in user
        } else {
            println("User email is missing, cannot fetch reminders")
        }
    }

    Scaffold(
        backgroundColor = Color.Transparent,
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            MultiFloatingActionButton(
                fabIcon = FabIcon(
                    iconRes = R.drawable.ic_baseline_add_24,
                    iconResAfterRotate = R.drawable.ic_baseline_remove_24,
                    iconRotate = 180f
                ),
                fabOption = FabOption(
                    iconTint = Color.White,
                    showLabels = true,
                    backgroundTint = MainBlue,
                ),
                itemsMultiFab = listOf(
                    MultiFabItem(
                        iconRes = R.drawable.pillemoji,
                        label = "복용 약 추가",
                        labelColor = Color.Black,
                        selectedDate = selectedDate
                    ),
                    MultiFabItem(
                        iconRes = R.drawable.hospitalemoji,
                        label = "진료 일정 추가",
                        labelColor = Color.Black,
                        selectedDate = selectedDate
                    ),
                ),
                onFabItemClicked = { println(it) },
                fabTitle = "MultiFloatActionButton",
                showFabTitle = false,
                navController = navController,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Surface(color = Color.Transparent) {
                DailyMedications(
                    navController = navController,
                    state = ReminderState(
                        reminders = reminders, // Use reminders fetched from the API
                        lastSelectedDate = SimpleDateFormat("yyyy-MM-dd").format(Date()) // Current date as default
                    ),
                    navigateToMedicationDetail = { },
                    onSelectedDate = { newDate -> selectedDate = newDate }, // Updated this line
                    logEvent = { /* 필요 시 추가 처리 */ },
                    reminderViewModel = reminderViewModel,
                    selectedDate = selectedDate
                )
            }
        }
    }
}


@Composable
fun DailyMedications(
    navController: NavController,
    state: ReminderState,
    navigateToMedicationDetail: (Reminder) -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
    logEvent: (String) -> Unit,
    reminderViewModel: ReminderViewModel,
    selectedDate: LocalDate
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 30.dp, bottom = 8.dp), // Padding around the entire column
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between children
    ) {
        // DatesHeader should be at the top
        DatesHeader(
            lastSelectedDate = state.lastSelectedDate,
            logEvent = { logEvent.invoke(it) },
            onDateSelected = { selectedDateModel ->
                val newDate = selectedDateModel.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() // LocalDate로 변환
                onSelectedDate(newDate) // LocalDate 전달
                logEvent.invoke(AnalyticsEvents.HOME_NEW_DATE_SELECTED)
            }
        )

        val dateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())

        val filteredReminders = state.reminders.filter { reminder ->
            reminder.medicationTime?.let {
                val medicationTimeFormatted = dateFormat.format(dateFormat.parse(it))
                val selectedDateFormatted = dateFormat.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                medicationTimeFormatted == selectedDateFormatted
            } ?: false // medicationTime이 null인 경우 false 반환
        }

        // Conditional content for medications
        if (filteredReminders.isEmpty()) {

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredReminders,
                    itemContent = { reminder ->
                        MedicationCard(
                            reminder = reminder,
                            navigateToMedicationDetail = { medication ->
                                navigateToMedicationDetail(medication)
                            },
                            deleteReminder = { reminderId ->
                                reminderViewModel.deleteReminder(reminderId)
                            },
                            navController = navController
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ManagementScreenPreview() {
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    ManagementScreen(navController = navController, btmBarViewModel = btmBarViewModel, userViewModel = UserViewModel())
}