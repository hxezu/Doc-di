package com.example.doc_di.home.dosage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.throttleFirst
import com.example.doc_di.reminder.data.MedicationData
import com.example.doc_di.search.SearchViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DosageList(
    medicationsForToday: List<MedicationData>,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    searchViewModel: SearchViewModel,
) {
    val titleColor = Color(0xFF404446)
    Column {
        Text(
            text = "복용 알림",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        if (medicationsForToday.isEmpty()) {
            val formattedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
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
                        painter = painterResource(id = R.drawable.home_clock),
                        contentDescription = "복용 알림 이미지",
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = "금일 복용할 약이 없습니다.",
                        fontSize = 14.sp,
                        color = Color(0xFF4A4A4A)
                    )
                    Button(
                        onClick = { { navController.navigate("${Routes.addMedicationScreen.route}?selectedDate=$formattedDate") }.throttleFirst() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B69FD))
                    ) {
                        Text(
                            text = "복용 약 등록하러 가기",
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
                    .padding(top = 16.dp)
            ) {
                items(medicationsForToday, key = { "${it.name}-${it.time}" }) { med ->
                    MedicationCardForToday(med, navController, btmBarViewModel, searchViewModel)
                }
            }
        }
    }
}