package com.example.doc_di.home.account_manage.modify_profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.example.doc_di.login.register.registerinfo.Visibility
import com.example.doc_di.login.register.registerinfo.VisibilityOff
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun ModifyPassword(
    password: MutableState<String>,
    passwordCheck: MutableState<String>,
    isPasswordAvailable: MutableState<Boolean>,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var passwordCheckHidden by rememberSaveable { mutableStateOf(true) }

    val labelSize = 14.sp
    val labelColor = Color(0xFF747F9E)

    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        label = {
            Text(
                text = "새 비밀번호",
                fontSize = labelSize,
                fontWeight = FontWeight.Bold,
                color = labelColor,
            )
        },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation()
        else VisualTransformation.None,
        placeholder = { Text(text = "새 비밀번호", color = Color.Black) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = LightBlue,
            cursorColor = MainBlue
        ),
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(
                    imageVector = if (passwordHidden) Visibility else VisibilityOff,
                    contentDescription = if (passwordHidden) "Show Password" else "Hide Password",
                    tint = Color.Gray
                )
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.9f)
    )

    OutlinedTextField(
        value = passwordCheck.value,
        onValueChange = { passwordCheck.value = it },
        label = {
            Text(
                text = "새 비밀번호 확인",
                fontSize = labelSize,
                fontWeight = FontWeight.Bold,
                color = labelColor,
            )
        },
        visualTransformation = if (passwordCheckHidden) PasswordVisualTransformation()
        else VisualTransformation.None,
        placeholder = { Text(text = "새 비밀번호화 동일하게 입력", color =  Color.Black) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = LightBlue,
            cursorColor = MainBlue
        ),
        trailingIcon = {
            IconButton(onClick = { passwordCheckHidden = !passwordCheckHidden }) {
                Icon(
                    imageVector = if (passwordCheckHidden) Visibility else VisibilityOff,
                    contentDescription = if (passwordCheckHidden) "Show Password" else "Hide Password",
                    tint = Color.Gray
                )
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.9f)
    )
    if (password.value.isNotEmpty() && passwordCheck.value.isNotEmpty()) {
        if (password.value == passwordCheck.value) {
            isPasswordAvailable.value = true
            Text(text = "비밀번호가 일치합니다.", color = Color(0xFF08630C))
        } else {
            isPasswordAvailable.value = false
            Text(text = "비밀번호가 일치하지 않습니다.", color = Color.Red)
        }
    }
}