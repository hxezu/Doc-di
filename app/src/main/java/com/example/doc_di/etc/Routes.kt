package com.example.doc_di.etc

sealed class Routes(val route: String) {
    object home: Routes("Home")
    object appointmentSchedule: Routes("AppointmentSchedule")
    object search: Routes("Search")
    object searchMethod: Routes("SearchMethod")
    object searchResult: Routes("SearchResult")
    object pillInformation: Routes("PillInformation")
    object medicalAppointmentRecord: Routes("MedicalAppointmentRecord")
    object prescriptionRecord: Routes("PrescriptionRecord")
    object prescribedMedicineList: Routes("PrescribedMedicineList")
    object chatListScreen: Routes("chatListScreen")
    object managementScreen: Routes("managementScreen")
    object profile: Routes("profile")
}