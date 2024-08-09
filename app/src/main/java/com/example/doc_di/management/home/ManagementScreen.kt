package com.example.doc_di.management.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.analytics.AnalyticsHelper
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.extension.toFormattedDateShortString
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.extension.toFormattedMonthDateString
import com.example.doc_di.management.addmedication.navigation.AddMedicationDestination
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.management.addmedication.AddMedicationScreenUI
import com.example.doc_di.management.home.data.CalendarDataSource
import com.example.doc_di.management.home.model.CalendarModel
import com.example.doc_di.management.home.viewmodel.ManagementState
import com.example.doc_di.management.home.viewmodel.ManagementViewModel
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManagementScreen(
    navController: NavController, 
    btmBarViewModel: BtmBarViewModel
) {
    val fabVisibility = rememberSaveable { (mutableStateOf(true)) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            if (fabVisibility.value) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between buttons
                        verticalAlignment = Alignment.Bottom
                    ) {
                        ScheduleFAB(navController)
                        DoseFAB(navController)
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
            paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            DailyMedications(
//                navController = navController,
//                state = state,
//                navigateToMedicationDetail = navigateToMedicationDetail,
//                onSelectedDate = onSelectedDate,
//                onDateSelected = onDateSelected,
//                logEvent = {
//                    logEvent.invoke(it)
//                },
//            )
            val calendar = Calendar.getInstance().apply {
                set(2024, Calendar.AUGUST, 5, 0, 0, 0) // Year, Month (0-based), Day, Hour, Minute, Second
                set(Calendar.MILLISECOND, 0)
            }
            val sampleDate = calendar.time
            val sampleDateString = SimpleDateFormat("yyyy-MM-dd").format(sampleDate)

            // Sample Data
            val sampleCalendarModel = CalendarModel(
                selectedDate = CalendarModel.DateModel(
                    date = sampleDate,
                    isSelected = true,
                    isToday = true
                ),
                visibleDates = List(7) { i ->
                    CalendarModel.DateModel(
                        date = calendar.apply { add(Calendar.DAY_OF_YEAR, i) }.time,
                        isSelected = i == 0, // Select the first date
                        isToday = i == 0 // Mark the first date as today
                    )
                }
            )

            val sampleMedications = listOf(
                Medication(
                    id = 1L,
                    name = "아스피린",
                    dosage = 2,
                    recurrence = "7",
                    endDate = calendar.apply { add(Calendar.DAY_OF_YEAR, 10) }.time,
                    medicationTaken = false,
                    medicationTime = calendar.apply { set(Calendar.HOUR_OF_DAY, 8); set(Calendar.MINUTE, 0) }.time
                ),
                Medication(
                    id = 2L,
                    name = "이부프로펜",
                    dosage = 1,
                    recurrence = "2",
                    endDate = calendar.apply { add(Calendar.DAY_OF_YEAR, 15) }.time,
                    medicationTaken = true,
                    medicationTime = calendar.apply { set(Calendar.HOUR_OF_DAY, 12); set(Calendar.MINUTE, 0) }.time
                )
            )

            val sampleState = ManagementState(
                medications = sampleMedications,
                lastSelectedDate = sampleDateString
            )

            // Mock ViewModel
            val mockViewModel = remember { BtmBarViewModel() }

            // Mock functions
            val navigateToMedicationDetail: (Medication) -> Unit = {}
            val onDateSelected: (CalendarModel.DateModel) -> Unit = {}
            val onSelectedDate: (Date) -> Unit = {}
            val logEvent: (String) -> Unit = {}

            // Render the DailyMedications with sample data
            Surface(color = MaterialTheme.colorScheme.background) {
                DailyMedications(
                    navController = rememberNavController(), // Use a rememberNavController for previews
                    state = sampleState,
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
fun ScheduleFAB(navController: NavController) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = "진료 일정", color = Color.White) },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                tint = Color.White,
                contentDescription = "Add"
            )
        },
        onClick = {
            navController.navigate(Routes.addScheduleScreen.route)
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        containerColor = MainBlue
    )
}

@Composable
fun DoseFAB(navController: NavController) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = "복용 약", color = Color.White) },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                tint = Color.White,
                contentDescription = "Add"
            )
        },
        onClick = {
            navController.navigate(Routes.addMedicationScreen.route)
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        containerColor = MainBlue
    )
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), // Padding around the entire column
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between children
    ) {
        // DatesHeader should be at the top
        DatesHeader(
            lastSelectedDate = state.lastSelectedDate,
            logEvent = { logEvent.invoke(it) },
            onDateSelected = { selectedDate ->
                onSelectedDate(selectedDate.date)
                logEvent.invoke(AnalyticsEvents.HOME_NEW_DATE_SELECTED)
            }
        )

        // Conditional content for medications
        if (state.medications.isEmpty()) {
            EmptyCard(
                navController = navController,
                logEvent = { logEvent.invoke(it) }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
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
            .height(156.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
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
                    text = "R.string.medication_break",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "home_screen_empty_card_message",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon), contentDescription = ""
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
                "Today"
            } else {
                data.selectedDate.date.toFormattedMonthDateString()
            },
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                tint = Color.Black,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                tint = Color.Black,
                contentDescription = "Next"
            )
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
                    MainBlue
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
                    color = if (date.isSelected) {
                        Color.White
                    } else {
                        Color.Black
                    },
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

sealed class MedicationListItem {
    data class OverviewItem(val medicationsToday: List<Medication>, val isMedicationListEmpty: Boolean) : MedicationListItem()
    data class MedicationItem(val medication: Medication) : MedicationListItem()
    data class HeaderItem(val headerText: String) : MedicationListItem()
}


@Preview(showBackground = true)
@Composable
fun ManagementScreenPreview() {
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    ManagementScreen(navController = navController, btmBarViewModel = btmBarViewModel)
}