package com.example.doc_di.etc

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.doc_di.chatbot.ChatbotScreen
import com.example.doc_di.home.HomeScreen
import com.example.doc_di.management.ManagementScreen
import com.example.doc_di.search.SearchScreen
import com.example.doc_di.searchresult.SearchResultScreen

@Composable
fun NaviGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HomeScreen.route){
        composable(route = Routes.HomeScreen.route){
            HomeScreen(navController = navController)
        }

        composable(route = Routes.searchScreen.route){
            SearchScreen(navController = navController)
        }

        composable(route = Routes.searchResultScreen.route){
            SearchResultScreen(navController = navController)
        }

        composable(route = Routes.chatbotScreen.route){
            ChatbotScreen(navController = navController)
        }

        composable(route = Routes.managementScreen.route){
            ManagementScreen(navController = navController)
        }
    }
}