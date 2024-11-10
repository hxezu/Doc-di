package com.example.doc_di.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.clickableThrottleFirst
import com.example.doc_di.etc.throttleFirst
import com.example.doc_di.reminder.data.AppointmentData
import com.example.doc_di.reminder.viewmodel.ReminderViewModel

@Composable
fun UpcomingAppointment(
    upcomingAppointment: List<AppointmentData>?,
    navController: NavController,
    reminderViewModel: ReminderViewModel,
) {
    val titleColor = Color(0xFF404446)

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 40.dp)
        ) {
            Text(
                text = "다가오는 진료 일정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickableThrottleFirst {
                    reminderViewModel.reservedTreatment = false
                    navController.navigate(Routes.appointmentSchedule.route)
                }
            ) {
                Text(text = "지난 진료 기록 보기", fontSize = 14.sp, color = Color(0xFF6C7072))
                Icon(
                    imageVector = Icons.Sharp.KeyboardArrowRight,
                    contentDescription = "지난 진료 기록 버튼",
                    tint = Color(0xFF6C7072)
                )
            }
        }
        if (upcomingAppointment.isNullOrEmpty()) {
            Card(
                colors = CardDefaults.cardColors(Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 40.dp)
                    .height(200.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_date),
                        contentDescription = "진료 일정 이미지",
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = "다가오는 진료 일정이 없습니다.",
                        fontSize = 14.sp,
                        color = Color(0xFF4A4A4A)
                    )
                    Button(
                        onClick = { { navController.navigate(Routes.addScheduleScreen.route) }.throttleFirst() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B69FD))
                    ) {
                        Text(
                            text = "진료 일정 등록하러 가기",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(240.dp)
                        )
                    }
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, end = 40.dp)
            ) {
                items(upcomingAppointment) { appointmentData ->
                    AppointmentCard(appointmentData, navController, reminderViewModel)
                }
            }
        }
    }
}