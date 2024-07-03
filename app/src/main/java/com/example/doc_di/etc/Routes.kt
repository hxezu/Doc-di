package com.example.doc_di.etc

sealed class Routes(val route: String) {
    object home: Routes("Home")
    object search: Routes("Search")
    object searchMethod: Routes("SearchMethod")
    object searchResult: Routes("SearchResult")
    object chatbotScreen: Routes("chatbotScreen")
    object managementScreen: Routes("managementScreen")
    object profile: Routes("profile")
}