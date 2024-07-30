package com.example.doc_di.management.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.extension.toFormattedDateShortString
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.extension.toFormattedMonthDateString
import com.example.doc_di.management.addmedication.navigation.AddMedicationDestination
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.management.home.data.CalendarDataSource
import com.example.doc_di.management.home.model.CalendarModel
import com.example.doc_di.management.home.viewmodel.ManagementState
import com.example.doc_di.management.home.viewmodel.ManagementViewModel
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.Calendar
import java.util.Date

@Composable
fun ManagementRoute(
    navController: NavController,
    askNotificationPermission: Boolean,
    askAlarmPermission: Boolean,
    navigateToMedicationDetail: (Medication) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ManagementViewModel = hiltViewModel()
) {
    val state by viewModel.homeUiState.collectAsState()
    PermissionAlarmDialog(
        askAlarmPermission = askAlarmPermission,
        logEvent = viewModel::logEvent
    )
    PermissionDialog(
        askNotificationPermission = askNotificationPermission,
        logEvent = viewModel::logEvent
    )
    ManagementScreen(
        modifier = modifier,
        navController = navController,
        state = state,
        navigateToMedicationDetail = navigateToMedicationDetail,
        onDateSelected = viewModel::selectDate,
        onSelectedDate = { viewModel.updateSelectedDate(it) },
        logEvent = viewModel::logEvent,
        btmBarViewModel = BtmBarViewModel()
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManagementScreen(
    modifier: Modifier,
    navController: NavController,
    state: ManagementState,
    navigateToMedicationDetail: (Medication) -> Unit,
    onDateSelected: (CalendarModel.DateModel) -> Unit,
    onSelectedDate: (Date) -> Unit,
    logEvent: (String) -> Unit,
    btmBarViewModel: BtmBarViewModel
) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DailyMedications(
                navController = navController,
                state = state,
                navigateToMedicationDetail = navigateToMedicationDetail,
                onSelectedDate = onSelectedDate,
                onDateSelected = onDateSelected,
                logEvent = {
                    logEvent.invoke(it)
                }
            )
        }
    }
}

@Composable
fun DailyMedications(
    navController: NavController,
    state: ManagementState,
    navigateToMedicationDetail: (Medication) -> Unit,
    onSelectedDate: (Date) -> Unit,
    onDateSelected: (CalendarModel.DateModel) -> Unit,
    logEvent: (String) -> Unit
) {

    DatesHeader(
        lastSelectedDate = state.lastSelectedDate,
        logEvent = {
            logEvent.invoke(it)
        },
        onDateSelected = { selectedDate ->
            onSelectedDate(selectedDate.date)
            logEvent.invoke(AnalyticsEvents.HOME_NEW_DATE_SELECTED)
        }
    )

    if (state.medications.isEmpty()) {
        EmptyCard(
            navController = navController,
            logEvent = {
                logEvent.invoke(it)
            }
        )
    } else {
        LazyColumn(
            modifier = Modifier,
        ) {
            items(
                items = state.medications,
                itemContent = {
                    MedicationCard(
                        medication = it,
                        navigateToMedicationDetail = { medication ->
                            navigateToMedicationDetail(medication)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun EmptyCard(
    navController: NavController,
    logEvent: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        logEvent.invoke(AnalyticsEvents.EMPTY_CARD_SHOWN)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp)
            .padding(vertical = 10.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = LightBlue,
            contentColor = MainBlue
        ),
        onClick = {
            logEvent.invoke(AnalyticsEvents.ADD_MEDICATION_CLICKED_EMPTY_CARD)
            navController.navigate(AddMedicationDestination.route)
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.50F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = "Medication Break",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "No medications scheduled for this date. Take a break and relax.",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

        }
    }
}

@Composable
fun DatesHeader(
    lastSelectedDate: String,
    onDateSelected: (CalendarModel.DateModel) -> Unit, // Callback to pass the selected date){}
    logEvent: (String) -> Unit
) {
    val dataSource = CalendarDataSource()
    var calendarModel by remember {
        mutableStateOf(
            dataSource.getData(lastSelectedDate = dataSource.getLastSelectedDate(lastSelectedDate))
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        DateHeader(
            data = calendarModel,
            onPrevClickListener = { startDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the startDate-1 from the visibleDates)
                val calendar = Calendar.getInstance()
                calendar.time = startDate

                calendar.add(Calendar.DAY_OF_YEAR, -2) // Subtract one day from startDate
                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
                logEvent.invoke(AnalyticsEvents.HOME_CALENDAR_PREVIOUS_WEEK_CLICKED)
            },
            onNextClickListener = { endDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the endDate+2 from the visibleDates)
                val calendar = Calendar.getInstance()
                calendar.time = endDate

                calendar.add(Calendar.DAY_OF_YEAR, 2)
                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
                logEvent.invoke(AnalyticsEvents.HOME_CALENDAR_NEXT_WEEK_CLICKED)
            }
        )
        DateList(
            data = calendarModel,
            onDateClickListener = { date ->
                calendarModel = calendarModel.copy(
                    selectedDate = date,
                    visibleDates = calendarModel.visibleDates.map {
                        it.copy(
                            isSelected = it.date.toFormattedDateString() == date.date.toFormattedDateString()
                        )
                    }
                )
                onDateSelected(date)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateItem(
    date: CalendarModel.DateModel,
    onClickListener: (CalendarModel.DateModel) -> Unit,
) {
    Column {
        Text(
            text = date.day, // day "Mon", "Tue"
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.outline
        )
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 4.dp),
            onClick = { onClickListener(date) },
            colors = cardColors(
                // background colors of the selected date
                // and the non-selected date are different
                containerColor = if (date.isSelected) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
        ) {
            Column(
                modifier = Modifier
                    .width(42.dp)
                    .height(42.dp)
                    .padding(8.dp)
                    .fillMaxSize(), // Fill the available size in the Column
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Text(
                    text = date.date.toFormattedDateShortString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (date.isSelected) {
                        FontWeight.Medium
                    } else {
                        FontWeight.Normal
                    }
                )
            }
        }
    }
}

@Composable
fun DateList(
    data: CalendarModel,
    onDateClickListener: (CalendarModel.DateModel) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = data.visibleDates) { date ->
            DateItem(date, onDateClickListener)
        }
    }
}

@Composable
fun DateHeader(
    data: CalendarModel,
    onPrevClickListener: (Date) -> Unit,
    onNextClickListener: (Date) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = if (data.selectedDate.isToday) {
                "TODAY"
            } else {
                data.selectedDate.date.toFormattedMonthDateString()
            },
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MainBlue
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Next"
            )
        }
    }
}

sealed class MedicationListItem {
    data class OverviewItem(val medicationsToday: List<Medication>, val isMedicationListEmpty: Boolean) : MedicationListItem()
    data class MedicationItem(val medication: Medication) : MedicationListItem()
    data class HeaderItem(val headerText: String) : MedicationListItem()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    askNotificationPermission: Boolean,
    logEvent: (String) -> Unit
) {
    if (askNotificationPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
        val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) { isGranted ->
            when (isGranted) {
                true -> logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_GRANTED)
                false -> logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_REFUSED)
            }
        }
        if (!notificationPermissionState.status.isGranted) {
            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {
                    logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_SHOWN)
                    AlertDialog(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        },
                        title = {
                            Text(text = "Notification Permission Required")
                        },
                        text = {
                            Text(text = "To ensure you never miss your medication, please grant the notification permission.")
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                            logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_DISMISSED)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    notificationPermissionState.launchPermissionRequest()
                                    openAlertDialog.value = false
                                    logEvent.invoke(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_ALLOW_CLICKED)
                                }
                            ) {
                                Text(text="Allow")
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAlarmDialog(
    askAlarmPermission: Boolean,
    logEvent: (String) -> Unit
) {
    val context = LocalContext.current
    val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
    if (askAlarmPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)) {
        val alarmPermissionState = rememberPermissionState(Manifest.permission.SCHEDULE_EXACT_ALARM) { isGranted ->
            when (isGranted) {
                true -> logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_GRANTED)
                false -> logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_REFUSED)
            }
        }
        if (alarmManager?.canScheduleExactAlarms() == false) {
            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {

                    logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_DIALOG_SHOWN)

                    AlertDialog(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        },
                        title = {
                            Text(text = "Alarms Permission Required")
                        },
                        text = {
                            Text(text = "To ensure you never miss your medication, please grant the alarms permission.")
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                            logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_DIALOG_DISMISSED)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    Intent().also { intent ->
                                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                                        context.startActivity(intent)
                                    }

                                    openAlertDialog.value = false
                                    logEvent.invoke(AnalyticsEvents.ALARM_PERMISSION_DIALOG_ALLOW_CLICKED)
                                }
                            ) {
                                Text(text="Allow")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(name = "Management Screen Preview", showBackground = true)
@Composable
fun ManagementScreenPreview() {
    val navController = rememberNavController()
    val dummyMedications = listOf(
        Medication(
            id = 1L,
            name = "Aspirin",
            dosage = 100,
            recurrence = "Daily",
            endDate = Date(),
            medicationTaken = false,
            medicationTime = Date()
        ),
        Medication(
            id = 2L,
            name = "Ibuprofen",
            dosage = 200,
            recurrence = "Twice a day",
            endDate = Date(),
            medicationTaken = true,
            medicationTime = Date()
        )
    )
    val dummyState = ManagementState(
        lastSelectedDate = "2024-07-28",
        medications = dummyMedications
    )
    ManagementScreen(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        state = dummyState,
        navigateToMedicationDetail = {},
        onDateSelected = {},
        onSelectedDate = {},
        logEvent = {},
        btmBarViewModel = BtmBarViewModel() // Provide a dummy instance or mock if needed
    )
}

@Preview(name = "Daily Medications Preview", showBackground = true)
@Composable
fun DailyMedicationsPreview() {
    val navController = rememberNavController()
    val dummyMedications = listOf(
        Medication(
            id = 1L,
            name = "Aspirin",
            dosage = 100,
            recurrence = "Daily",
            endDate = Date(),
            medicationTaken = false,
            medicationTime = Date()
        ),
        Medication(
            id = 2L,
            name = "Ibuprofen",
            dosage = 200,
            recurrence = "Twice a day",
            endDate = Date(),
            medicationTaken = true,
            medicationTime = Date()
        )
    )
    val dummyState = ManagementState(
        lastSelectedDate = "2024-07-28",
        medications = dummyMedications
    )
    DailyMedications(
        navController = navController,
        state = dummyState,
        navigateToMedicationDetail = {},
        onSelectedDate = {},
        onDateSelected = {},
        logEvent = {}
    )
}


@Preview(name = "Empty Card Preview", showBackground = true)
@Composable
fun EmptyCardPreview() {
    val navController = rememberNavController()
    EmptyCard(
        navController = navController,
        logEvent = {}
    )
}

@Preview(name = "Dates Header Preview", showBackground = true)
@Composable
fun DatesHeaderPreview() {
    val calendarModel = CalendarModel(
        selectedDate = CalendarModel.DateModel(
            date = Date(),
            isSelected = true,
            isToday = true
        ),
        visibleDates = listOf(
            CalendarModel.DateModel(
                date = Date(),
                isSelected = true,
                isToday = true
            ),
            CalendarModel.DateModel(
                date = Date(System.currentTimeMillis() + 86400000), // Tomorrow
                isSelected = false,
                isToday = false
            )
        )
    )
    DatesHeader(
        lastSelectedDate = "2024-07-28",
        onDateSelected = {},
        logEvent = {}
    )
}

