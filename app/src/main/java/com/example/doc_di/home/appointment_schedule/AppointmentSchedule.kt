package com.example.doc_di.home.appointment_schedule

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.GoBack
import com.example.doc_di.reminder.data.AppointmentData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppointmentSchedule(
    upcomingAppointment: List<AppointmentData>?,
    pastAppointment: List<AppointmentData>?,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
) {
    val reservedTreatment = remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(top = 68.dp)
        ) {
            GoBack(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.height(24.dp))
            AppointmentScheduleHeader(navController)
            Spacer(modifier = Modifier.height(34.dp))
            UpcomingOrPastButton(reservedTreatment)
            Spacer(modifier = Modifier.height(40.dp))
            if (reservedTreatment.value) {
                if (upcomingAppointment.isNullOrEmpty()){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 106.dp)
                    ) {
                        Text(text = "예정된 진료 일정이 없습니다. 건강하시네요!")
                    }
                }
                else{
                    UpcomingAppointmentList(upcomingAppointment)
                }
            }
            else {
                if (pastAppointment.isNullOrEmpty()){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 106.dp)
                    ) {
                        Text(text = "이전 진료 기록이 없습니다. 건강하시네요!")
                    }
                }
                else{
                    PastAppointmentList(pastAppointment)
                }
            }
        }
    }
}