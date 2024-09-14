package com.example.doc_di.reminder.medicationconfirm.navigation

import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.reminder.medicationconfirm.MedicationConfirmRoute
import com.example.doc_di.navigation.DoseNavigationDestination

const val MEDICATION = "medication"

object MedicationConfirmDestination : DoseNavigationDestination {
    override val route = "medication_confirm_route"
    override val destination = "medication_confirm_destination"
}

fun NavGraphBuilder.medicationConfirmGraph(navController: NavController, bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>, onBackClicked: () -> Unit, navigateToHome: () -> Unit) {

    composable(
        route = MedicationConfirmDestination.route,
    ) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = false
            fabVisibility.value = false
        }
        val medicationBundle = navController.previousBackStackEntry?.savedStateHandle?.get<Bundle>(MEDICATION)
        val reminderList = medicationBundle?.getParcelableArrayList<Reminder>(MEDICATION)
        MedicationConfirmRoute(reminderList, onBackClicked, navigateToHome)
    }
}