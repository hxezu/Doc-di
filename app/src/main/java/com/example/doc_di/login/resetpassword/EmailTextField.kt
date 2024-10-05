package com.example.doc_di.login.resetpassword

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun EmailTextField(email: MutableState<String>) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = email.value,
        onValueChange = { email.value = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                text = "이메일",
                color = Color.Black,
                style = MaterialTheme.typography.labelMedium
            )
        },
        placeholder = { Text(text = "가입 시 입력한 이메일") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = LightBlue,
            cursorColor = MainBlue
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.8f)
    )
}