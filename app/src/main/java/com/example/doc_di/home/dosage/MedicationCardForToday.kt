package com.example.doc_di.home.dosage

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.etc.throttleFirst
import com.example.doc_di.reminder.data.MedicationData
import com.example.doc_di.search.SearchViewModel

@Composable
fun MedicationCardForToday(
    medication: MedicationData,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    searchViewModel: SearchViewModel
) {
    val cardPillColor = Color(0xFF202325)
    val alarmColor = Color(0xFF979C9E)
    val pinColor = Color(0xFF979C9E)
    val cardTextColor = Color(0xFF72777A)
    val context = LocalContext.current

    Card(
        onClick = {
            {
                if (isNetworkAvailable(context)) {
                    searchViewModel.setSelectedPillByPillName(medication.name)
                    btmBarViewModel.btmNavBarItems[0].selected = false
                    btmBarViewModel.btmNavBarItems[1].selected = true
                    navController.navigate(Routes.searchResult.route)
                }
                else {
                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            }.throttleFirst()
        },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .width(200.dp)
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
                    text = medication.time,
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
                    fontSize = 15.sp,
                    text = "${medication.endDate} 까지",
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
                    text = medication.dosage,
                    color = cardTextColor
                )
            }
        }
    }
}
