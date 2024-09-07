package com.example.doc_di.management.home.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun DoseFAB(navController: NavController) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = "복용 약", color = Color.White) },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                tint = Color.White,
                contentDescription = "Add"
            )
        },
        onClick = {
            navController.navigate(Routes.addMedicationScreen.route)
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        containerColor = MainBlue
    )
}

@Composable
fun ScheduleFAB(navController: NavController) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = "진료 일정", color = Color.White) },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                tint = Color.White,
                contentDescription = "Add"
            )
        },
        onClick = {
            navController.navigate(Routes.addScheduleScreen.route)
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        containerColor = MainBlue
    )
}