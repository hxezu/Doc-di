package com.example.doc_di.home.appointment_schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun AppointmentScheduleHeader(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "진료 일정",
            fontSize = 38.sp,
            color = Color(0xFF191A26),
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.add_button),
            contentDescription = "진료 일정 추가",
            modifier = Modifier
                .size(42.dp)
                .clickable {
                    navController.navigate(Routes.addScheduleScreen.route)
                }
        )
    }
}