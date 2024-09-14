package com.example.doc_di.reminder.medicationdetail

import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.reminder.medicationconfirm.navigation.MEDICATION
import com.example.doc_di.navigation.DoseNavigationDestination

object MedicationDetailDestination : DoseNavigationDestination {
    override val route = "medication_detail_route"
    override val destination = "medication_detail_destination"
}

fun NavGraphBuilder.medicationDetailGraph(navController: NavController, bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>, onBackClicked: () -> Unit) {

    composable(
        route = MedicationDetailDestination.route,
    ) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = false
            fabVisibility.value = false
        }
        val medicationBundle = navController.previousBackStackEntry?.savedStateHandle?.get<Bundle>(
            MEDICATION
        )
        val reminder = medicationBundle?.getParcelable<Reminder>(MEDICATION)
        MedicationDetailRoute(reminder, onBackClicked)
    }
}