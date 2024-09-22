package com.example.doc_di.reminder.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.navigation.DoseNavigationDestination

const val ASK_NOTIFICATION_PERMISSION = "notification_permission"
const val ASK_ALARM_PERMISSION = "alarm_permission"
object HomeDestination : DoseNavigationDestination {
    override val route = "home_route"
    override val destination = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    bottomBarVisibility: MutableState<Boolean>,
    fabVisibility: MutableState<Boolean>,
    navigateToMedicationDetail: (Reminder) -> Unit
) {
    composable(route = HomeDestination.route) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = true
            fabVisibility.value = true
        }

        val askNotificationPermission = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(ASK_NOTIFICATION_PERMISSION) ?: false
        val askAlarmPermission = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(ASK_ALARM_PERMISSION) ?: false

        // Obtain the viewModel for ManagementRoute
        val viewModel: ReminderViewModel = hiltViewModel()

//        // Pass the viewModel parameter to ManagementRoute
//        ManagementRoute(
//            navController = navController,
//            askNotificationPermission = askNotificationPermission,
//            askAlarmPermission = askAlarmPermission,
//            navigateToMedicationDetail = navigateToMedicationDetail,
//            viewModel = viewModel // Pass the ManagementViewModel here
//        )
    }
}