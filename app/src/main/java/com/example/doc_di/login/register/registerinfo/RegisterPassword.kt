package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.doc_di.login.Visibility
import com.example.doc_di.login.VisibilityOff
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun RegisterPassword(
    password: MutableState<String>,
    passwordCheck: MutableState<String>,
    isPasswordAvailable: MutableState<Boolean>,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var passwordCheckHidden by rememberSaveable { mutableStateOf(true) }

    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                text = "비밀번호",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        placeholder = { Text(text = "비밀번호") },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MainBlue
        ),
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(
                    imageVector = if (passwordHidden) Visibility else VisibilityOff,
                    contentDescription = if (passwordHidden) "Show Password" else "Hide Password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    OutlinedTextField(
        value = passwordCheck.value,
        onValueChange = { passwordCheck.value = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                text = "비밀번호 확인",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        placeholder = { Text(text = "비밀번호와 동일하게 입력") },
        visualTransformation = if (passwordCheckHidden) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MainBlue
        ),
        trailingIcon = {
            IconButton(onClick = { passwordCheckHidden = !passwordCheckHidden }) {
                Icon(
                    imageVector = if (passwordCheckHidden) Visibility else VisibilityOff,
                    contentDescription = if (passwordCheckHidden) "Show Password" else "Hide Password"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(0.8f)
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