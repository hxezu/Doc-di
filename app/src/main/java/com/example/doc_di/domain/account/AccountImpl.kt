package com.example.doc_di.domain.account

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.login.removeAccessToken
import com.example.doc_di.login.removeRefreshToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class AccountImpl(private val accountApi: AccountApi) {
    suspend fun modifyProfile(
        email: String,
        password: String,
        name: String,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController,
        bitmap: MutableState<Bitmap?>,
        userViewModel: UserViewModel,
    ) {
        val accessToken = userViewModel.checkAccessAndReissue(context, navController)
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                val accountDTO = AccountDTO(
                    email = email,
                    password = password,
                    name = name,
                    image = email
                )
                val file = File(context.cacheDir, "${email}.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.value!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }

                val accountJson = Gson().toJson(accountDTO)
                val accountRequestBody = accountJson.toRequestBody("application/json".toMediaTypeOrNull())
                val accountPart = MultipartBody.Part.createFormData("userDto", null, accountRequestBody)

                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val modifyResponse = accountApi.modifyProfile(accountPart, filePart, accessToken!!)
                if (modifyResponse.isSuccessful) {
                    userViewModel.fetchUser(context, navController){
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "프로필 수정 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.home.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "모든 정보를 정확히 기입해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun logoutAccount(
        context: Context,
        navController: NavController,
        userViewModel: UserViewModel
    ){
        val accessToken = userViewModel.checkAccessAndReissue(context, navController)
        val logoutResponse = accountApi.logout(accessToken!!)
        if (logoutResponse.isSuccessful) {
            userViewModel.clearUserData()
            removeAccessToken(context)
            removeRefreshToken(context)
            navController.navigate(Routes.login.route) {
                popUpTo(Routes.login.route) {
                    inclusive = true
                }
            }
        }
    }

    suspend fun deleteAccount(
        context: Context,
        navController: NavController,
        userViewModel: UserViewModel
    ){
        val accessToken = userViewModel.checkAccessAndReissue(context, navController)
        if (userViewModel.userInfo.value != null && accessToken != null) {
            val deleteResponse = accountApi.deleteAccount( userViewModel.userInfo.value!!.email, accessToken)
            if (deleteResponse.isSuccessful) {
                userViewModel.clearUserData()
                removeAccessToken(context)
                removeRefreshToken(context)
                navController.navigate(Routes.login.route) {
                    popUpTo(Routes.login.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}