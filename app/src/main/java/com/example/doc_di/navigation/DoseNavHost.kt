package com.example.doc_di.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.reminder.addmedication.navigation.addMedicationGraph
import com.example.doc_di.reminder.calendar.navigation.calendarGraph
import com.example.doc_di.reminder.history.historyGraph
import com.example.doc_di.reminder.home.navigation.HomeDestination
import com.example.doc_di.reminder.home.navigation.homeGraph
import com.example.doc_di.reminder.medicationconfirm.navigation.MEDICATION
import com.example.doc_di.reminder.medicationconfirm.navigation.MedicationConfirmDestination
import com.example.doc_di.reminder.medicationconfirm.navigation.medicationConfirmGraph
import com.example.doc_di.reminder.medicationdetail.MedicationDetailDestination
import com.example.doc_di.reminder.medicationdetail.medicationDetailGraph
import com.example.doc_di.util.navigateSingleTop

@Composable
fun DoseNavHost(
    bottomBarVisibility: MutableState<Boolean>,
    fabVisibility: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            navigateToMedicationDetail = {
                val bundle = Bundle()
                bundle.putParcelable(MEDICATION, it)
                navController.currentBackStackEntry?.savedStateHandle.apply {
                    this?.set(MEDICATION, bundle)
                }
                navController.navigate(MedicationDetailDestination.route)
            }
        )
        historyGraph(
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            navigateToMedicationDetail = {
                val bundle = Bundle()
                bundle.putParcelable(MEDICATION, it)
                navController.currentBackStackEntry?.savedStateHandle.apply {
                    this?.set(MEDICATION, bundle)
                }
                navController.navigate(MedicationDetailDestination.route)
            }
        )
        medicationDetailGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            onBackClicked = { navController.navigateUp() }
        )
        calendarGraph(bottomBarVisibility, fabVisibility)
        addMedicationGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            onBackClicked = { navController.navigateUp() },
            navigateToMedicationConfirm = {
                // TODO: Replace with medication id
                val bundle = Bundle()
                bundle.putParcelableArrayList(MEDICATION, ArrayList(it))
                navController.currentBackStackEntry?.savedStateHandle.apply {
                    this?.set(MEDICATION, bundle)
                }
                navController.navigate(MedicationConfirmDestination.route)
            }
        )
        medicationConfirmGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            onBackClicked = { navController.navigateUp() },
            navigateToHome = {
                navController.navigateSingleTop(HomeDestination.route)
            }
        )
    }
}