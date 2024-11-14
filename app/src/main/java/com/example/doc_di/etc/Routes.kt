package com.example.doc_di.etc

sealed class Routes(val route: String) {
    object login : Routes("LoginPage")
    object register : Routes("RegisterPage")
    object resetPassword : Routes("ResetPassword")
    object home: Routes("Home")
    object appointmentSchedule: Routes("AppointmentSchedule")
    object search: Routes("Search")
    object searchMethod: Routes("SearchMethod")
    object searchResult: Routes("SearchResult")
    object pillInformation: Routes("PillInformation")
    object chatListScreen: Routes("ChatListScreen")
    object chatScreen: Routes("ChatScreen")
    object managementScreen: Routes("ManagementScreen")
    object addMedicationScreen: Routes("AddMedicationScreen")
    object editMedicationScreen: Routes("EditMedicationScreen")
    object editScheduleScreen: Routes("EditScheduleScreen")
    object addScheduleScreen: Routes("AddScheduleScreen")
    object profile: Routes("profile")
    object modifyLogoutAccountDelete: Routes("ModifyLogoutAccountDelete")
    object appDescription: Routes("AppDescription")
    object chatbotSearchResult: Routes("ChatbotSearchResult")
}