package com.example.doc_di.management.history

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.doc_di.domain.model.Medication
import com.example.doc_di.navigation.DoseNavigationDestination

object HistoryDestination : DoseNavigationDestination {
    override val route = "history_route"
    override val destination = "history_destination"
}

fun NavGraphBuilder.historyGraph(bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>, navigateToMedicationDetail: (Medication) -> Unit) {
    composable(route = HistoryDestination.route) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = true
            fabVisibility.value = false
        }
        HistoryRoute(navigateToMedicationDetail)
    }
}