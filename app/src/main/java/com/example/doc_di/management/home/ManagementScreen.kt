package com.example.doc_di.management.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.management.home.model.CalendarModel
import com.example.doc_di.management.home.utils.DatesHeader
import com.example.doc_di.management.home.viewmodel.ManagementState
import com.example.doc_di.management.home.utils.EmptyCard
import com.example.doc_di.management.home.utils.FabIcon
import com.example.doc_di.management.home.utils.FabOption
import com.example.doc_di.management.home.utils.MedicationCard
import com.example.doc_di.management.home.utils.MultiFabItem
import com.example.doc_di.management.home.utils.MultiFloatingActionButton
import com.example.doc_di.ui.theme.MainBlue
import java.text.SimpleDateFormat
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
            Surface(color = Color.Transparent) {
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
            .padding(start = 8.dp, end = 8.dp, top = 30.dp, bottom = 8.dp), // Padding around the entire column
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