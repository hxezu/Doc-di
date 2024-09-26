package com.example.doc_di.home.appointment_schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.data.AppointmentData
import com.example.doc_di.reminder.viewmodel.ReminderViewModel

@Composable
fun UpcomingAppointmentList(
    upcomingAppointment: List<AppointmentData>,
    navController: NavController,
    reminderViewModel: ReminderViewModel
) {
    var dropdownStates by remember { mutableStateOf(List(upcomingAppointment.size) { false }) }
    val options = listOf("수정", "삭제")

    val dateColor = Color(0xFF191D30)
    val textColor = Color(0xFF333333)
    val dateBackgroundColor = Color(0xFFF5F5F5)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 106.dp)
    ) {
        itemsIndexed(upcomingAppointment) { index, appointmentData ->
            Card(
                onClick = { navController.navigate(Routes.editScheduleScreen.route + "/${appointmentData.appointmentId}") },
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = appointmentData.hospitalName,
                            color = textColor,
                            fontSize = 13.sp
                        )
                        Box {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "더 보기",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        dropdownStates = dropdownStates.mapIndexed { i, expanded ->
                                            if (i == index) !expanded else false
                                        }
                                    }
                            )
                            DropdownMenu(
                                expanded = dropdownStates[index],
                                onDismissRequest = {
                                    dropdownStates = dropdownStates.map { false }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .background(Color.White)
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        onClick = {
                                            if (option == "수정") {
                                                navController.navigate(Routes.editScheduleScreen.route + "/${appointmentData.appointmentId}")
                                            } else if (option == "삭제") {
                                                reminderViewModel.deleteBookedReminder(appointmentData.appointmentId)
                                            }
                                            dropdownStates = dropdownStates.map { false }
                                        }
                                    ) {
                                        Text(text = option)
                                    }
                                }
                            }
                        }
                    }
                    Card(
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(dateBackgroundColor)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 6.dp, horizontal = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.date),
                                contentDescription = "날짜",
                                tint = dateColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = appointmentData.formattedTime,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.eye),
                            contentDescription = "안과 아이콘",
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = appointmentData.subject,
                            color = Color.LightGray,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = appointmentData.doctorName,
                            color = textColor,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}