package com.example.doc_di.etc

sealed class Routes(val route: String) {
    object HomeScreen: Routes("HomeScreen")
    object searchScreen: Routes("SearchScreen")
    object searchResultScreen: Routes("SearchResultScreen")
    object chatbotScreen: Routes("chatbotScreen")
    object managementScreen: Routes("managementScreen")
}