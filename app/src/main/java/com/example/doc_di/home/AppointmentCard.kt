package com.example.doc_di.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.data.AppointmentData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentCard(index: Int, appointmentData: AppointmentData, navController: NavController) {
    val cardProfessorColor = Color(0xFF202325)
    val treatmentTimeColor = Color(0xFF404446)
    val locateColor = Color(0xFF6C7072)
    val departmentColor = Color(0xFF5555CB)
    val appointmentCardColors = listOf(Color(0xFFF0F0FF), Color(0xFFFFF9F0))
    val appointmentCardImageIds = listOf(R.drawable.doctor_image, R.drawable.warm_up_image)

    val date = appointmentData.formattedTime.split(" ")
    val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    val tomorrowDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    val appointmentDate = when (date[0]) {
        todayDate -> "오늘 ${date[1]} ${date[2]}"
        tomorrowDate -> "내일 ${date[1]} ${date[2]}"
        else -> appointmentData.formattedTime
    }
    val cardColor = if (index % 2 == 0) appointmentCardColors[0] else appointmentCardColors[1]
    val cardImageId = if (index % 2 == 0) appointmentCardImageIds[0] else appointmentCardImageIds[1]
    Box(modifier = Modifier.width(280.dp)) {
        Card(
            onClick = { navController.navigate(Routes.appointmentSchedule.route) },
            colors = CardDefaults.cardColors(cardColor),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier
                .width(280.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = appointmentData.doctorName,
                        color = cardProfessorColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = appointmentDate,
                        color = treatmentTimeColor,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(72.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치 아이콘",
                            tint = locateColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = appointmentData.hospitalName,
                            color = locateColor,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Box(
                        modifier = Modifier
                            .size(width = 70.dp, height = 26.dp)
                            .clip(shape = RoundedCornerShape(48.dp))
                            .background(Color.White)
                    ) {
                        Text(
                            text = appointmentData.subject,
                            color = departmentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = cardImageId),
            contentDescription = "의사 이미지",
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 36.dp)
        )
    }
}