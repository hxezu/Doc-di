package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.LoadingHomeScreen
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.reminder.data.AppointmentData
import androidx.compose.runtime.livedata.observeAsState
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import kotlin.system.exitProcess


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    upcomingAppointment: List<AppointmentData>?,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    reminderViewModel: ReminderViewModel
) {
    val titleColor = Color(0xFF404446)
    val userInfo = userViewModel.userInfo.value

    val medicationsForToday by reminderViewModel.medicationsForToday.observeAsState(emptyList())

    fun updateBtmBarItem(route: String) {
        btmBarViewModel.btmNavBarItems.forEach {
            it.selected = route == it.route
        }
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                Routes.home.route -> { updateBtmBarItem(Routes.home.route) }
                Routes.search.route -> { updateBtmBarItem(Routes.search.route) }
                Routes.chatListScreen.route -> { updateBtmBarItem(Routes.chatListScreen.route) }
                Routes.managementScreen.route -> { updateBtmBarItem(Routes.managementScreen.route) }
            }
        }

    }

    BackHandler {
        if (navController.previousBackStackEntry?.destination?.route == null) {
            exitProcess(0)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent,
    ) {
        if (userInfo == null) {
            LoadingHomeScreen()
        } else {
            LaunchedEffect(userInfo.email) {
                reminderViewModel.getBookedReminders(userInfo.email)
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, bottom = 106.dp, top = 40.dp)
            ) {
                HomeGreeting(navController, userViewModel)
                UpcomingAppointment(upcomingAppointment, navController)

                Column {
                    Text(
                        text = "복용 알림",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        // medicationsForToday 리스트를 순회하며 Card 생성
                        items(medicationsForToday, key = { "${it.name}-${it.time}" }) { med ->
                            MedicationCardForToday(medication = med, navController = navController, btmBarViewModel = btmBarViewModel)
                        }
                    }
                }
            }
        }
    }
}
