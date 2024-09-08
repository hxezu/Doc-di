package com.example.doc_di.management.home.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.extension.toFormattedTimeString
import com.example.doc_di.management.addmedication.navigation.AddMedicationDestination
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationCard(
    medication: Medication,
    navigateToMedicationDetail: (Medication) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),

    ) {

        Text(
            medication.medicationTime.toFormattedTimeString(),
            color = Color.Black, // 검정색 설정
            fontWeight = FontWeight.Bold )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .border(
                    border = BorderStroke(width = 0.5.dp, color = Color(0xFFECEDEF)), // Black border with 2.dp width
                    shape = RoundedCornerShape(30.dp) // Same shape as the card
                ),
            onClick = { navigateToMedicationDetail(medication) },
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment
                    .CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    painter = painterResource(id = R.drawable.pillemoji),
                    contentDescription = "용법 이미지"
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    modifier = Modifier
                        .weight(3f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = medication.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = medication.dosage.toString() + " 정",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }

                Text(
                    text = medication.recurrence + " 일간",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1.5f)
                )
            }
        }
    }

}

@Preview
@Composable
private fun MedicationCardTakeNowPreview() {
    MedicationCard(
        Medication(
            id = 123L,
            name = "오메가 3",
            dosage = 1,
            recurrence = "2",
            endDate = Date(),
            medicationTime = Date(),
            medicationTaken = false
        )
    ) { }
}

@Preview
@Composable
private fun MedicationCardTakenPreview() {
    MedicationCard(
        Medication(
            id = 123L,
            name = "소염제",
            dosage = 1,
            recurrence = "2",
            endDate = Date(),
            medicationTime = Date(),
            medicationTaken = true
        )
    ) { }
}

@Composable
fun EmptyCard(
    navController: NavController,
    logEvent: (String) -> Unit
) {

    LaunchedEffect(Unit) {
        logEvent.invoke(AnalyticsEvents.EMPTY_CARD_SHOWN)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            logEvent.invoke(AnalyticsEvents.ADD_MEDICATION_CLICKED_EMPTY_CARD)
            navController.navigate(AddMedicationDestination.route)
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.50F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = "R.string.medication_break",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "home_screen_empty_card_message",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon), contentDescription = ""
                )
            }
        }
    }
}