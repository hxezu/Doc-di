package com.example.doc_di.domain.resetpw

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.etc.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetImpl {
    suspend fun resetPassword(email: String, context: Context, navController: NavController) {
        val resetResponse = RetrofitInstance.resetApi.resetPassword(email)
        if (resetResponse.isSuccessful) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "비밀번호 재발급 성공", Toast.LENGTH_SHORT).show()
            }
            navController.navigate(Routes.login.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "유효하지 않은 이메일", Toast.LENGTH_SHORT).show()
            }
        }
    }
}