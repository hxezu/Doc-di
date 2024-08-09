package com.example.doc_di.etc

import android.util.StatsLog.logEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.doc_di.LoginPage
import com.example.doc_di.RegisterPage
import com.example.doc_di.ResetPage
import com.example.doc_di.chatbot.ChatListScreen
import com.example.doc_di.domain.pillsearch.PillsSearchRepositoryImpl
import com.example.doc_di.domain.pillsearch.RetrofitInstance
import com.example.doc_di.home.AppointmentSchedule
import com.example.doc_di.home.Home
import com.example.doc_di.home.Profile
import com.example.doc_di.management.addmedication.AddMedicationScreenUI
import com.example.doc_di.management.addschedule.AddScheduleScreenUI
import com.example.doc_di.management.home.ManagementScreen
import com.example.doc_di.search.MedicalAppointmentRecord
import com.example.doc_di.search.PrescribedMedicineList
import com.example.doc_di.search.PrescriptionRecord
import com.example.doc_di.search.Search
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.searchmethod.SearchMethod
import com.example.doc_di.searchresult.PillInformation
import com.example.doc_di.searchresult.PillInformationViewModel
import com.example.doc_di.searchresult.SearchResult

@Composable
fun NaviGraph(navController: NavHostController) {

    val searchViewModel: SearchViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(PillsSearchRepositoryImpl(RetrofitInstance.api)) as T
        }
    })

    val pillViewModel: PillInformationViewModel = viewModel()
    val btmBarViewModel: BtmBarViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.login.route) {

        composable(Routes.login.route) {
            LoginPage(navController = navController)
        }

        composable(Routes.register.route) {
            RegisterPage(navController = navController)
        }

        composable(Routes.reset.route) {
            ResetPage(navController = navController)
        }

        composable(route = Routes.home.route) {
            Home(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.appointmentSchedule.route) {
            AppointmentSchedule(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.profile.route) {
            Profile(navController = navController)
        }

        composable(route = Routes.search.route) {
            Search(
                navController = navController,
                searchViewModel = searchViewModel,
                btmBarViewModel = btmBarViewModel
            )
        }

        composable(route = Routes.searchMethod.route) {
            SearchMethod(
                navController = navController,
                searchViewModel = searchViewModel,
                btmBarViewModel = btmBarViewModel
            )
        }

        composable(route = Routes.searchResult.route) {
            SearchResult(
                navController = navController,
                btmBarViewModel = btmBarViewModel,
                searchViewModel = searchViewModel
            )
        }

        composable(route = Routes.pillInformation.route) {
            PillInformation(
                navController = navController,
                pillViewModel = pillViewModel,
                btmBarViewModel = btmBarViewModel,
                searchViewModel = searchViewModel
            )
        }

        composable(route = Routes.medicalAppointmentRecord.route) {
            MedicalAppointmentRecord(
                navController = navController,
                btmBarViewModel = btmBarViewModel
            )
        }

        composable(route = Routes.prescriptionRecord.route) {
            PrescriptionRecord(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.prescribedMedicineList.route) {
            PrescribedMedicineList(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.chatListScreen.route) {
            ChatListScreen(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.managementScreen.route) {
            ManagementScreen(
                navController = navController,
                btmBarViewModel = btmBarViewModel
            )
        }

        composable(route = Routes.addMedicationScreen.route) {
            AddMedicationScreenUI(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.addScheduleScreen.route) {
            AddScheduleScreenUI(navController = navController, btmBarViewModel = btmBarViewModel)
        }
    }
}
