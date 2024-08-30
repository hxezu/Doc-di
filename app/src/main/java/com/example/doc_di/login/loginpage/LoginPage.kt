package com.example.doc_di.login.loginpage

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.login.LoginImpl
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.GradientButton
import kotlinx.coroutines.launch

@Composable
fun LoginPage(navController: NavController) {
    val loginImpl = LoginImpl(RetrofitInstance.loginApi)

    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val loginCheck = rememberSaveable { mutableStateOf(false) }

    val gradientColor = listOf(Color(0xFF0052D4), Color(0xFF4364F7), Color(0xFF6FB1FC))
    val cornerRadius = 16.dp

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
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
            if (loginCheck.value) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "아이디 또는 비밀번호를 잘못 입력했습니다.\n입력하신 내용을 다시 확인해주세요.",
                    fontSize = 12.sp,
                    color = Color.Red,
                    lineHeight = 16.sp
                )
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
            GradientButton(
                onClick = {
                    scope.launch {
                        try {
                            loginImpl.login(email.value, password.value, context, navController, loginCheck)
                        } catch (e: Exception) {
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