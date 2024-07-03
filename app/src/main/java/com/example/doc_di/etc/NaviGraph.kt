package com.example.doc_di.etc

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.doc_di.chatbot.ChatbotScreen
import com.example.doc_di.home.Home
import com.example.doc_di.home.Profile
import com.example.doc_di.management.ManagementScreen
import com.example.doc_di.search.Search
import com.example.doc_di.search.SearchMethod
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.searchresult.SearchResult

@Composable
fun NaviGraph(navController: NavHostController) {
    val searchViewModel : SearchViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.home.route){
        composable(route = Routes.home.route){
            Home(navController = navController)
        }
        composable(route = Routes.profile.route){
            Profile(navController= navController)
        }

        composable(route = Routes.search.route){
            Search(navController = navController ,searchViewModel = searchViewModel)
        }

        composable(route = Routes.searchMethod.route){
            SearchMethod(navController = navController, searchViewModel = searchViewModel)
        }

        composable(route = Routes.searchResult.route){
            SearchResult(navController = navController)
        }

        composable(route = Routes.chatbotScreen.route){
            ChatbotScreen(navController = navController)
        }

        composable(route = Routes.managementScreen.route){
            ManagementScreen(navController = navController)
        }
    }
}