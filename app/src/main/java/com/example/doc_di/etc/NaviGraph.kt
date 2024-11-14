package com.example.doc_di.etc

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.doc_di.chatbot.ChatBotViewModel
import com.example.doc_di.chatbot.ChatListScreen
import com.example.doc_di.chatbot.ChatScreen
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.chatbot.ChatBotImpl
import com.example.doc_di.domain.chatbot.ChatRepository
import com.example.doc_di.domain.pill.PillsSearchRepositoryImpl
import com.example.doc_di.home.Home
import com.example.doc_di.home.account_manage.AppDescription
import com.example.doc_di.home.account_manage.ModifyLogoutAccountDelete
import com.example.doc_di.home.account_manage.modify_profile.Profile
import com.example.doc_di.home.appointment_schedule.AppointmentSchedule
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.login.loginpage.LoginPage
import com.example.doc_di.login.register.RegisterPage
import com.example.doc_di.login.resetpassword.ResetPassword
import com.example.doc_di.reminder.booked_reminder.AddScheduleScreen
import com.example.doc_di.reminder.booked_reminder.EditScheduleScreen
import com.example.doc_di.reminder.home.ReminderScreen
import com.example.doc_di.reminder.medication_reminder.AddMedicationScreenUI
import com.example.doc_di.reminder.medication_reminder.EditMedicationScreen
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.search.Search
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchmethod.SearchMethod
import com.example.doc_di.search.pillsearch.searchresult.ChatbotSearchResult
import com.example.doc_di.search.pillsearch.searchresult.SearchResult
import com.example.doc_di.search.pillsearch.searchresult.pill_information.PillInformation
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NaviGraph(navController: NavHostController) {
    val searchViewModel: SearchViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(PillsSearchRepositoryImpl(RetrofitInstance.pillApi)) as T
        }
    })

    val chatBotViewModel: ChatBotViewModel =
        viewModel(factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val chatRepository = ChatRepository()
                val chatBotImpl =
                    ChatBotImpl(RetrofitInstance.chatBotApi) // Create instance of ChatBotImpl
                return ChatBotViewModel(
                    chatRepository,
                    chatBotImpl
                ) as T // Pass it to the ViewModel
            }
        })

    val btmBarViewModel: BtmBarViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val reviewViewModel: ReviewViewModel = viewModel()
    val reminderViewModel: ReminderViewModel = viewModel()

    val pastAppointment by reminderViewModel.pastAppointments.observeAsState()
    val upcomingAppointment by reminderViewModel.upcomingAppointments.observeAsState()


    NavHost(navController = navController, startDestination = Routes.login.route) {

        composable(Routes.login.route) {
            LoginPage(navController, userViewModel, reminderViewModel)
        }

        composable(Routes.register.route) {
            RegisterPage(navController)
        }

        composable(Routes.resetPassword.route) {
            ResetPassword(navController)
        }

        composable(route = Routes.home.route) {
            Home(
                upcomingAppointment,
                navController,
                btmBarViewModel,
                userViewModel,
                reminderViewModel,
                searchViewModel
            )
        }

        composable(Routes.appointmentSchedule.route) {
            AppointmentSchedule(
                upcomingAppointment,
                pastAppointment,
                navController,
                btmBarViewModel,
                reminderViewModel
            )
        }

        composable(
            route = Routes.modifyLogoutAccountDelete.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            ModifyLogoutAccountDelete(navController, userViewModel)
        }

        composable(route = Routes.profile.route) {
            Profile(navController, userViewModel)
        }

        composable(route = Routes.appDescription.route){
            AppDescription(navController)
        }

        composable(route = Routes.search.route) {
            Search(navController, searchViewModel, btmBarViewModel, userViewModel)
        }

        composable(route = Routes.searchMethod.route) {
            SearchMethod(navController, searchViewModel, btmBarViewModel)
        }

        composable(route = Routes.searchResult.route) {
            SearchResult(
                navController,
                btmBarViewModel,
                userViewModel,
                searchViewModel,
                reviewViewModel,
                reminderViewModel
            )
        }

        composable(route = Routes.chatbotSearchResult.route) {
            ChatbotSearchResult(
                navController,
                btmBarViewModel,
                userViewModel,
                searchViewModel,
                reviewViewModel,
                reminderViewModel,
                chatBotViewModel
            )
        }

        composable(route = Routes.pillInformation.route) {
            PillInformation(
                navController,
                btmBarViewModel,
                searchViewModel,
                userViewModel,
                reviewViewModel,
                reminderViewModel
            )
        }

        composable(route = Routes.chatListScreen.route) {
            ChatListScreen(
                navController,
                btmBarViewModel,
                userViewModel,
                chatBotViewModel
            )
        }

        // 채팅 상세 화면: 특정 채팅 ID를 인자로 받음
        composable(
            route = "chat_screen/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.IntType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt("chatId") ?: 0
            ChatScreen(
                navController = navController,
                btmBarViewModel = btmBarViewModel,
                userViewModel = userViewModel,
                chatBotViewModel = chatBotViewModel,
                chatId = chatId,
                searchViewModel = searchViewModel,
                reviewViewModel = reviewViewModel
            )
        }

        // 채팅 상세 화면: 새로운 대화 (chatId 없음)
        composable(route = "chat_screen") {
            ChatScreen(
                navController = navController,
                btmBarViewModel = btmBarViewModel,
                userViewModel = userViewModel,
                chatBotViewModel = chatBotViewModel,
                chatId = null,
                searchViewModel = searchViewModel,
                reviewViewModel = reviewViewModel
            )
        }


        composable(route = Routes.managementScreen.route) {
            ReminderScreen(
                navController,
                btmBarViewModel,
                reminderViewModel,
                userViewModel,
                searchViewModel,
                reviewViewModel
            )
        }

        composable(route = "addMedicationScreen?selectedDate={selectedDate}") { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate")
            AddMedicationScreenUI(
                navController = navController,
                btmBarViewModel = btmBarViewModel,
                userViewModel = userViewModel,
                selectedDateString = selectedDate
            )
        }

        composable(route = "editMedicationScreen/{reminderId}") { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getString("reminderId")?.toIntOrNull()
            EditMedicationScreen(navController, btmBarViewModel, userViewModel, reminderViewModel, reminderId)
        }

        composable(route = "editScheduleScreen/{reminderId}") { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getString("reminderId")?.toIntOrNull()
            EditScheduleScreen(navController, btmBarViewModel, reminderViewModel, reminderId)
        }

        composable(route = "addScheduleScreen?selectedDate={selectedDate}") { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate")
            AddScheduleScreen(
                navController = navController,
                btmBarViewModel = btmBarViewModel,
                userViewModel = userViewModel,
                selectedDateString = selectedDate
            )
        }
    }
}
