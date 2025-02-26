package com.example.doc_di.domain.login

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.login.saveAccessToken
import com.example.doc_di.login.saveRefreshToken
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginImpl(private val loginApi: LoginApi) {
    suspend fun login(
        username: String,
        password: String,
        context: Context,
        navController: NavController,
        loginCheck: MutableState<Boolean>,
        userViewModel: UserViewModel,
        reminderViewModel: ReminderViewModel
    ) {
        val loginDTO = LoginDTO(
            username = username,
            password = password
        )
        val response = loginApi.login(loginDTO)
        if (response.isSuccessful) {
            val accessToken = response.headers()["access"]
            accessToken?.let {
                println("accesToken: $accessToken")
                saveAccessToken(context, accessToken)
            }

            val cookies = response.headers()["Set-Cookie"]
            cookies?.let { cookies ->
                val cookie = cookies.split(";")[0]
                val refreshToken = cookie.split("=")[1]
                println("refreshToken: $refreshToken")
                saveRefreshToken(context, refreshToken)
            }

            userViewModel.fetchUser(context, navController){
                userViewModel.userInfo.value?.let { userInfo ->
                    reminderViewModel.getBookedReminders(userInfo.email)
                    reminderViewModel.getReminders(userInfo.email)
                    println("Success to Fetch Reminders")
                }
            }
            
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
            navController.navigate(Routes.home.route){
                popUpTo(navController.graph.startDestinationId){
                    inclusive = true
                }
                launchSingleTop = true
            }
            loginCheck.value = false
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
            loginCheck.value = true
        }
    }
}