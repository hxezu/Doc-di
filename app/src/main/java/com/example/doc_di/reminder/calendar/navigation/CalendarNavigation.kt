package com.example.doc_di.reminder.calendar.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.doc_di.reminder.calendar.CalendarRoute
import com.example.doc_di.navigation.DoseNavigationDestination

object CalendarDestination : DoseNavigationDestination {
    override val route = "calendar_route"
    override val destination = "calendar_destination"
}

fun NavGraphBuilder.calendarGraph(bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>) {
    composable(route = CalendarDestination.route) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = true
            fabVisibility.value = false
        }
        CalendarRoute()
    }
}