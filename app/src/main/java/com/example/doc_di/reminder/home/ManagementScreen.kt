package com.example.doc_di.reminder.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.reminder.home.model.CalendarModel
import com.example.doc_di.reminder.home.utils.DatesHeader
import com.example.doc_di.reminder.home.viewmodel.ReminderState
import com.example.doc_di.reminder.home.utils.EmptyCard
import com.example.doc_di.reminder.home.utils.FabIcon
import com.example.doc_di.reminder.home.utils.FabOption
import com.example.doc_di.reminder.home.utils.MedicationCard
import com.example.doc_di.reminder.home.utils.MultiFabItem
import com.example.doc_di.reminder.home.utils.MultiFloatingActionButton
import com.example.doc_di.reminder.home.viewmodel.ReminderViewModel
import com.example.doc_di.ui.theme.MainBlue
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManagementScreen(
    navController: NavController, 
    btmBarViewModel: BtmBarViewModel,
    reminderViewModel: ReminderViewModel = hiltViewModel()
) {
    val reminders by reminderViewModel.reminders

    // Trigger API call when the screen is first composed
    LaunchedEffect(Unit) {
        reminderViewModel.getReminders("test@example.com") // Fetch reminders for the user
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
                    ),
                    MultiFabItem(
                        iconRes = R.drawable.hospitalemoji,
                        label = "진료 일정 추가",
                        labelColor = Color.Black
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

            val navigateToMedicationDetail: (Reminder) -> Unit = {}
            val onDateSelected: (CalendarModel.DateModel) -> Unit = {}
            val onSelectedDate: (Date) -> Unit = {}
            val logEvent: (String) -> Unit = {}

            Surface(color = Color.Transparent) {
                DailyMedications(
                    navController = navController,
                    state = ReminderState(
                        reminders = reminders, // Use reminders fetched from the API
                        lastSelectedDate = SimpleDateFormat("yyyy-MM-dd").format(Date()) // Current date as default
                    ),
                    navigateToMedicationDetail = navigateToMedicationDetail,
                    onDateSelected = onDateSelected,
                    onSelectedDate = onSelectedDate,
                    logEvent = logEvent
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
    onSelectedDate: (Date) -> Unit,
    onDateSelected: (CalendarModel.DateModel) -> Unit,
    logEvent: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

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
                selectedDate = selectedDateModel.date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                onSelectedDate(selectedDateModel.date)
                logEvent.invoke(AnalyticsEvents.HOME_NEW_DATE_SELECTED)
            }
        )

        // 선택된 날짜에 맞는 알림 필터링
        val filteredReminders = state.reminders.filter { reminder ->
            // reminder의 날짜와 선택된 날짜가 같은지 비교 (시간을 제외한 날짜만 비교)
            val reminderDate = reminder.medicationTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            reminderDate == selectedDate
        }

        // Conditional content for medications
        if (filteredReminders.isEmpty()) {
            EmptyCard(
                navController = navController,
                logEvent = { logEvent.invoke(it) }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredReminders,
                    itemContent = {
                        MedicationCard(
                            reminder = it,
                            navigateToMedicationDetail = { medication ->
                                navigateToMedicationDetail(medication)
                            }
                        )
                    }
                )
            }
        }
    }
}


sealed class MedicationListItem {
    data class OverviewItem(val medicationsToday: List<Reminder>, val isMedicationListEmpty: Boolean) : MedicationListItem()
    data class MedicationItem(val reminder: Reminder) : MedicationListItem()
    data class HeaderItem(val headerText: String) : MedicationListItem()
}

@Preview(showBackground = true)
@Composable
fun ManagementScreenPreview() {
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    ManagementScreen(navController = navController, btmBarViewModel = btmBarViewModel)
}