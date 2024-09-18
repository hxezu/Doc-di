package com.example.doc_di.login.resetpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.domain.resetpw.ResetImpl
import com.example.doc_di.login.GradientButton
import com.example.doc_di.ui.theme.LightBlue
import kotlinx.coroutines.launch

@Composable
fun ResetPassword(navController: NavController) {
    val gradientColor = listOf(Color(0xFF0052D4), Color(0xFF4364F7), Color(0xFF6FB1FC))
    val email = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val resetImpl = ResetImpl()
    val cornerRadius = 16.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(2f))
        Text(
            text = "비밀번호 재설정",
            style = MaterialTheme.typography.headlineSmall,
            color = LightBlue,
        )
        EmailTextField(email)
        GradientButton(
            onClick = {
                scope.launch {
                    resetImpl.resetPassword(email.value, context, navController)
                }
            },
            gradientColors = gradientColor,
            cornerRadius = cornerRadius,
            roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
            buttonName = "제출"
        )
        TextButton(
            onClick = {
                navController.navigate("RegisterPage") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        ) {
            Text(
                text = "회원가입하러 가기",
                letterSpacing = 1.sp,
                style = MaterialTheme.typography.labelLarge,
                color = LightBlue
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}