package com.example.doc_di.login.loginpage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.domain.model.LoginReq
import com.example.doc_di.domain.pillsearch.RetrofitInstance
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.GradientButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginPage(navController: NavController) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var loginCheck by rememberSaveable { mutableStateOf(false) }

    val gradientColor = listOf(Color(0xFF0052D4), Color(0xFF4364F7), Color(0xFF6FB1FC))
    val cornerRadius = 16.dp

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.nameicon),
                contentDescription = "로고",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 100.dp)
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(40.dp))
            EmailTextField(email)
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField(password)
            if (loginCheck) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "아이디 또는 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.",
                    fontSize = 12.sp,
                    color = Color.Red,
                    lineHeight = 16.sp
                )
            }
            else {
                Spacer(modifier = Modifier.height(20.dp))
            }
            GradientButton(
                onClick = {
                    scope.launch {
                        try {
                            val loginReq = LoginReq(
                                username = email.value,
                                password = password.value
                            )
                            val response = RetrofitInstance.api.login(loginReq)
                            if (response.isSuccessful){
                                val accessToken = response.headers()["access"]
                                accessToken?.let {
                                    println("accesToken: $accessToken")
                                    saveAccessToken(context, accessToken)
                                }

                                val cookies = response.headers()["Set-Cookie"]
                                cookies?.let { cookies->
                                    val cookie = cookies.split(";")[0]
                                    val refreshToken = cookie.split("=")[1]
                                    println("refreshToken: $refreshToken")
                                    saveRefreshToken(context, refreshToken)
                                }

                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                                }
                                navController.navigate(Routes.home.route)
                            }
                            else{
                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                                }
                                loginCheck = true
                            }
                        }catch(e: Exception){
                            e.printStackTrace()
                        }
                    }
                },
                gradientColors = gradientColor,
                cornerRadius = cornerRadius,
                roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                buttonName = "로그인"
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(onClick = { navController.navigate(Routes.register.route) }) {
                Text(
                    text = "회원가입",
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = { navController.navigate(Routes.reset.route) }) {
                Text(
                    text = "아이디/비밀번호 찾기",
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}