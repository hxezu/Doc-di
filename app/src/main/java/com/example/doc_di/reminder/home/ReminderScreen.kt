package com.example.doc_di.reminder.home

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.reminder.data.ReminderItem
import com.example.doc_di.reminder.home.utils.BookedCard
import com.example.doc_di.reminder.home.utils.DatesHeader
import com.example.doc_di.reminder.home.state.ReminderState
import com.example.doc_di.reminder.home.utils.FabIcon
import com.example.doc_di.reminder.home.utils.FabOption
import com.example.doc_di.reminder.home.utils.MedicationCard
import com.example.doc_di.reminder.home.utils.MultiFabItem
import com.example.doc_di.reminder.home.utils.MultiFloatingActionButton
import com.example.doc_di.reminder.home.state.BookedReminderState
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import com.example.doc_di.ui.theme.MainBlue
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReminderScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    userViewModel: UserViewModel,
    searchViewModel: SearchViewModel,
    reviewViewModel: ReviewViewModel
) {
    val reminders by reminderViewModel.reminders
    val bookedReminders by reminderViewModel.bookedReminders
    val userInfo by userViewModel.userInfo.observeAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(Unit) {
        if(userInfo != null){
                println("Fetching reminders for user: ${userViewModel.userInfo.value!!.email}")
                reminderViewModel.getBookedReminders(userViewModel.userInfo.value!!.email)
                reminderViewModel.getReminders(userViewModel.userInfo.value!!.email) // Fetch reminders for the logged-in user
        }else{
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
                onFabItemClicked = { println(it)
                },
                fabTitle = "MultiFloatActionButton",
                showFabTitle = false,
                navController = navController,
                modifier = Modifier.padding(bottom = 20.dp),
                selectedDate = selectedDate
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
                    bookedState = BookedReminderState(
                        bookedReminders = bookedReminders ,
                        lastSelectedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
                    ),
                    navigateToMedicationDetail = { },
                    onSelectedDate = { newDate ->
                        selectedDate = newDate
                                     }, // Updated this line
                    logEvent = { /* 필요 시 추가 처리 */ },
                    reminderViewModel = reminderViewModel,
                    selectedDate = selectedDate,
                    searchViewModel = searchViewModel,
                    reviewViewModel = reviewViewModel
                )
            }
        }
    }
}


@Composable
fun DailyMedications(
    navController: NavController,
    state: ReminderState,
    bookedState: BookedReminderState,
    navigateToMedicationDetail: (Reminder) -> Unit,
    onSelectedDate: (LocalDate) -> Unit,
    logEvent: (String) -> Unit,
    reminderViewModel: ReminderViewModel,
    selectedDate: LocalDate,
    searchViewModel: SearchViewModel,
    reviewViewModel: ReviewViewModel
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
                onSelectedDate(newDate)// LocalDate 전달
                logEvent.invoke(AnalyticsEvents.HOME_NEW_DATE_SELECTED)
            }
        )

        val dateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())

        // Filter medication reminders
        val filteredReminders = state.reminders.filter { reminder ->
            reminder.medicationTime?.let {
                val medicationTimeFormatted = dateFormat.format(dateFormat.parse(it))
                val selectedDateFormatted = dateFormat.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                medicationTimeFormatted.startsWith(selectedDateFormatted) // 날짜 일치 확인
            } ?: false
        }

        // Filter booked reminders
        val filteredBookedReminders = bookedState.bookedReminders.filter { booked ->
            booked.bookTime?.let {
                val bookedTimeFormatted = dateFormat.format(dateFormat.parse(it))
                val selectedDateFormatted = dateFormat.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                bookedTimeFormatted.startsWith(selectedDateFormatted) // 날짜 일치 확인
            } ?: false
        }

        // Combine filtered reminders
        val combinedReminders = (filteredReminders.map { ReminderItem.ReminderType.Medication(it) } +
                filteredBookedReminders.map { ReminderItem.ReminderType.Clinic(it) })
            .sortedWith(compareBy<ReminderItem> {
                when (it) {
                    is ReminderItem.ReminderType.Medication -> {
                        it.reminder.medicationTime?.let { timeStr ->
                            val time = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(timeStr)?.time
                            time ?: Long.MAX_VALUE
                        } ?: Long.MAX_VALUE
                    }
                    is ReminderItem.ReminderType.Clinic -> {
                        it.booked.bookTime?.let { timeStr ->
                            val time = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(timeStr)?.time
                            time ?: Long.MAX_VALUE
                        } ?: Long.MAX_VALUE
                    }
                }
            })


        if (combinedReminders.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(combinedReminders) { reminderItem ->
                    when (reminderItem) {
                        is ReminderItem.ReminderType.Medication -> {
                            MedicationCard(
                                reminder = reminderItem.reminder,
                                navigateToMedicationDetail = { medication -> navigateToMedicationDetail(medication) },
                                deleteReminder = { reminderId -> reminderViewModel.deleteReminder(reminderId) },
                                navController = navController,
                                searchViewModel = searchViewModel,
                                reviewViewModel = reviewViewModel
                            )
                        }
                        is ReminderItem.ReminderType.Clinic -> {
                            BookedCard(
                                booked = reminderItem.booked,
                                navigateToMedicationDetail = { bookedDetail -> /* Navigate to booked detail */ },
                                deleteBookedReminder = { bookedId -> reminderViewModel.deleteBookedReminder(bookedId) },
                                navController = navController
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // This should create space at the bottom
                }
            }
        }
        
    }
}
