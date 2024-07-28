package com.example.doc_di.etc

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.doc_di.LoginPage
import com.example.doc_di.RegisterPage
import com.example.doc_di.ResetPage
import com.example.doc_di.chatbot.ChatListScreen
import com.example.doc_di.chatbot.ChatScreen
import com.example.doc_di.home.AppointmentSchedule
import com.example.doc_di.home.BtmBarViewModel
import com.example.doc_di.home.Home
import com.example.doc_di.home.Profile
import com.example.doc_di.management.AddPillScreen
import com.example.doc_di.management.ManagementScreen
import com.example.doc_di.search.MedicalAppointmentRecord
import com.example.doc_di.search.PrescribedMedicineList
import com.example.doc_di.search.PrescriptionRecord
import com.example.doc_di.search.Search
import com.example.doc_di.search.SearchMethod
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.searchresult.PillInformation
import com.example.doc_di.searchresult.PillInformationViewModel
import com.example.doc_di.searchresult.SearchResult

@Composable
fun NaviGraph(navController: NavHostController) {
    val searchViewModel : SearchViewModel = viewModel()
    val pillViewModel : PillInformationViewModel = viewModel()
    val btmBarViewModel :BtmBarViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.login.route){
        composable(Routes.login.route) {
            LoginPage(navController = navController)
        }

        composable(Routes.register.route) {
            RegisterPage(navController = navController)
        }

        composable(Routes.reset.route) {
            ResetPage(navController = navController)
        }

        composable(route = Routes.home.route){
            Home(navController = navController, btmBarViewModel = btmBarViewModel)
        }
        
        composable(route = Routes.appointmentSchedule.route){
            AppointmentSchedule(navController = navController, btmBarViewModel = btmBarViewModel)
        }
        
        composable(route = Routes.profile.route){
            Profile(navController= navController)
        }

        composable(route = Routes.search.route){
            Search(navController = navController ,searchViewModel = searchViewModel, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.searchMethod.route){
            SearchMethod(navController = navController, searchViewModel = searchViewModel, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.searchResult.route){
            SearchResult(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.pillInformation.route){
            PillInformation(navController = navController, pillViewModel = pillViewModel, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.medicalAppointmentRecord.route){
            MedicalAppointmentRecord(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.prescriptionRecord.route){
            PrescriptionRecord(navController = navController, btmBarViewModel = btmBarViewModel)
        }
        
        composable(route = Routes.prescribedMedicineList.route){
            PrescribedMedicineList(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.chatListScreen.route){
            ChatListScreen(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.chatScreen.route){
            ChatScreen(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(route = Routes.managementScreen.route){
            ManagementScreen(navController = navController, btmBarViewModel = btmBarViewModel)
        }

        composable(
            route = "${Routes.addPillScreen.route}/{userId}/{date}/{time}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            AddPillScreen(
                navController = navController,
                userId = userId,
                startDate = date,
                takeTime = time,
                onSaveTask = { /* Implement onSaveTask logic here */ }
            )
        }
    }
}