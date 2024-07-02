package com.example.doc_di.etc

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.doc_di.chatbot.ChatbotScreen
import com.example.doc_di.home.Home
import com.example.doc_di.home.Profile
import com.example.doc_di.management.ManagementScreen
import com.example.doc_di.search.Search
import com.example.doc_di.searchresult.SearchResult

@Composable
fun NaviGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.home.route){
        composable(route = Routes.home.route){
            Home(navController = navController)
        }
        composable(route = Routes.profile.route){
            Profile(navController= navController)
        }

        composable(route = Routes.search.route){
            Search(navController = navController)
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