package com.example.doc_di.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.data.AppointmentData

@Composable
fun UpcomingAppointment(upcomingAppointment: List<AppointmentData>?, navController: NavController) {
    val titleColor = Color(0xFF404446)

    Column{
        Text(
            text = "다가오는 진료 일정",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 40.dp)
        ) {
            if (upcomingAppointment.isNullOrEmpty()){
                item {
                    Card(
                        onClick = { navController.navigate(Routes.addScheduleScreen.route) },
                        colors = CardDefaults.cardColors(Color.Transparent),
                        elevation = CardDefaults.cardElevation(1.dp),
                        shape = RoundedCornerShape(2),
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(160.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "진료 일정을 추가해 보세요!",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
            else{
                itemsIndexed(upcomingAppointment){ index, appointmentData ->
                    AppointmentCard(index, appointmentData, navController)
                }
            }
        }
    }
}