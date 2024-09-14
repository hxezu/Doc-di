package com.example.doc_di.domain.reminder

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderImpl(private val reminderApi: ReminderApi) {
    suspend fun createReminder(
        email: String,
        medicineName: String,
        dosage: Short,
        recurrence: String,
        endDate: String,
        medicationTime: String,
        medicationTaken: String,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController
    ) {
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                val reminderDTO = ReminderDTO(
                    email = email,
                    medicineName = medicineName,
                    dosage = dosage,
                    recurrence = recurrence,
                    endDate = endDate,
                    medicationTime = medicationTime,
                    medicationTaken = medicationTaken,
                )

                val reminderResponse = reminderApi.createReminder(reminderDTO)

                if (reminderResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        println("알림 등록 성공")
                        Toast.makeText(context, "알림 등록 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.managementScreen.route) { navController.popBackStack() }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        println("알림 등록 실패")
                        Toast.makeText(context, "알림 등록 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "모든 정보를 정확히 기입해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}