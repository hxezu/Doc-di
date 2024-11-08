package com.example.doc_di.login.resetpassword

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.domain.resetpw.ResetImpl
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.login.GradientButton
import com.example.doc_di.login.rememberImeState
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

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value){
        if (imeState.value){
            scrollState.animateScrollTo(0)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            )
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
                if(isNetworkAvailable(context)) {
                    scope.launch {
                        resetImpl.resetPassword(email.value, context, navController)
                    }
                }
                else{
                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }

            },
            gradientColors = gradientColor,
            cornerRadius = cornerRadius,
            roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
            buttonName = "제출",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
        )
        TextButton(
            onClick = {
                navController.navigate("RegisterPage") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                keyboardController?.hide()
            },
        ) {
            Text(
                text = "회원가입 하러가기",
                letterSpacing = 1.sp,
                style = MaterialTheme.typography.labelLarge,
                color = LightBlue
            )
        }
        if (!imeState.value){
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}