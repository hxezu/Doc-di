package com.example.doc_di.home.appointment_schedule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.etc.throttleFirst

@Composable
fun UpcomingOrPastButton(reservedTreatment: MutableState<Boolean>) {
    val reservedCardColor = if (reservedTreatment.value) Color.White else Color(0xFFF6F7FA)
    val reservedCardTextColor =
        if (reservedTreatment.value) Color(0xFF242B42) else Color(0xFF1D1E25)
    val reservedCardTextFontWeight =
        if (reservedTreatment.value) FontWeight.Bold else FontWeight.Normal
    val reservedCardElevation =
        if (reservedTreatment.value) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(
            0.dp
        )

    val pastCardColor = if (!reservedTreatment.value) Color.White else Color(0xFFF6F7FA)
    val pastCardTextColor = if (!reservedTreatment.value) Color(0xFF242B42) else Color(0xFF1D1E25)
    val pastCardTextFontWeight =
        if (!reservedTreatment.value) FontWeight.Bold else FontWeight.Normal
    val pastCardElevation =
        if (!reservedTreatment.value) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(
            0.dp
        )

    Card(
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(Color(0xFFF6F7FA)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 3.dp)
        ) {
            Card(
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(reservedCardColor),
                elevation = reservedCardElevation,
                onClick = { { reservedTreatment.value = true }.throttleFirst() },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "예약된 진료",
                    fontSize = 14.sp,
                    fontWeight = reservedCardTextFontWeight,
                    color = reservedCardTextColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Card(
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(pastCardColor),
                elevation = pastCardElevation,
                onClick = { { reservedTreatment.value = false }.throttleFirst() },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "지난 진료",
                    fontSize = 14.sp,
                    fontWeight = pastCardTextFontWeight,
                    color = pastCardTextColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}