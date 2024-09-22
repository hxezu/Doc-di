package com.example.doc_di.reminder.home.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MiniFabItem(
    item: MultiFabItem,
    showLabel: Boolean,
    miniFabBackgroundColor: Color,
    navController: NavController,
    selectedDate: LocalDate,
    onFabItemClicked: (item: MultiFabItem) -> Unit
) {
    println("Selected Date in MiniFabItem: $selectedDate")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        FloatingActionButton(
            modifier = Modifier.size(40.dp),
            onClick = {
                onFabItemClicked(item)
                val formattedDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                println("Button Selected Date: $formattedDate")
                when (item.iconRes) {
                    R.drawable.pillemoji -> navController.navigate("${Routes.addMedicationScreen.route}?selectedDate=$formattedDate")
                    R.drawable.hospitalemoji -> navController.navigate("${Routes.addScheduleScreen.route}?selectedDate=$formattedDate")
                }
                      },
            backgroundColor = miniFabBackgroundColor
        ) {
            Box(modifier = Modifier.padding(8.dp)) { // Add padding here
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = "multifab ${item.label}",
                    tint = Color.Unspecified
                )
            }
        }
        if (showLabel) {
            Text(
                item.label,
                fontSize = 12.sp,
                color = item.labelColor,
                modifier = Modifier
                    .padding(start = 10.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    .widthIn(min = 80.dp, max = 120.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}