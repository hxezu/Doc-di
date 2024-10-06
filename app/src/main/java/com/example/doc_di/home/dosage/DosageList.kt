package com.example.doc_di.home.dosage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.reminder.data.MedicationData
import com.example.doc_di.search.SearchViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DosageList(
    medicationsForToday: List<MedicationData>,
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    searchViewModel: SearchViewModel
) {
    val titleColor = Color(0xFF404446)
    Column {
        Text(
            text = "복용 알림",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (medicationsForToday.isEmpty()) {
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                item {
                    Card(
                        onClick = { navController.navigate("${Routes.addMedicationScreen.route}?selectedDate=$formattedDate") },
                        colors = CardDefaults.cardColors(Color.Transparent),
                        elevation = CardDefaults.cardElevation(1.dp),
                        shape = RoundedCornerShape(2),
                        modifier = Modifier
                            .size(180.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "약을 등록해 주세요!",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            } else {
                items(medicationsForToday, key = { "${it.name}-${it.time}" }) { med ->
                    MedicationCardForToday(med, navController, btmBarViewModel, searchViewModel)
                }
            }
        }
    }
}