package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.LoadingHomeScreen
import com.example.doc_di.etc.Routes
import com.example.doc_di.home.dosage.DosageList
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.reminder.data.AppointmentData
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.search.SearchViewModel
import kotlin.system.exitProcess


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    upcomingAppointment: List<AppointmentData>?,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    reminderViewModel: ReminderViewModel,
    searchViewModel: SearchViewModel
) {
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
                reminderViewModel.getReminders(userInfo.email)
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, bottom = 106.dp, top = 40.dp)
            ) {
                HomeGreeting(navController, userViewModel)
                UpcomingAppointment(upcomingAppointment, navController)
                DosageList(medicationsForToday,navController, btmBarViewModel, searchViewModel)
            }
        }
    }
}
