package com.example.doc_di.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.data.MedicationData

@Composable
fun MedicationCardForToday(
    medication: MedicationData,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel
) {
    val cardPillColor = Color(0xFF202325)
    val alarmColor = Color(0xFF979C9E)
    val pinColor = Color(0xFF979C9E)
    val starColor = Color(0xFFFFC462)
    val cardTextColor = Color(0xFF72777A)

    Card(
        onClick = {
            // BottomNavigationBar의 선택 상태 업데이트
            btmBarViewModel.btmNavBarItems.forEach {
                it.selected = false
            }
            btmBarViewModel.btmNavBarItems[1].selected = true
            // 복용 알림 상세 화면으로 이동 (Routes.pillInformation.route를 사용)
            navController.navigate(Routes.pillInformation.route)
        },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .width(180.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = medication.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = cardPillColor
            )
            Spacer(modifier = Modifier.size(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.alarm),
                    contentDescription = "복용 알람 아이콘",
                    tint = alarmColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = " ${medication.time}",
                    color = cardTextColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "핀 아이콘",
                    tint = pinColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${medication.endDate}까지",
                    color = cardTextColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${medication.dosage}정",
                    color = cardTextColor
                )
            }
        }
    }
}
