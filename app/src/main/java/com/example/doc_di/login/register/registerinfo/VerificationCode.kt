package com.example.doc_di.login.register.registerinfo

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.register.RegisterImpl
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue
import kotlinx.coroutines.launch

@Composable
fun VerificationCode(
    email: MutableState<String>,
    verifyCode: MutableState<String>,
    context: Context
) {
    val registerImpl = RegisterImpl(RetrofitInstance.registerApi)
    val scope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {
        OutlinedTextField(
            value = verifyCode.value,
            onValueChange = { verifyCode.value = it },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            label = {
                Text(
                    text = "인증코드",
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            placeholder = { Text(text = "이메일 코드 입력", color = Color.Black) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = LightBlue,
                cursorColor = MainBlue
            ),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .height(64.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "인증코드 받기",
            fontSize = 15.sp,
            color = Color.Black,
            letterSpacing = 1.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(top = 9.dp)
                .width(100.dp)
                .clickable {
                    if (isNetworkAvailable(context)) {
                        scope.launch {
                            registerImpl.makeCode(email.value, context)
                        }
                    }
                    else {
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }
        )
    }
}