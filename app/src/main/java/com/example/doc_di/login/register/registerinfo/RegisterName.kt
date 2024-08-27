package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun RegisterName(name: MutableState<String>, isNameAvailable: MutableState<Boolean>) {
    var userNameError by rememberSaveable { mutableStateOf<String?>(null) }
    val nameRegex = "^[가-힣a-zA-Z]{1,10}$".toRegex()

    OutlinedTextField(
        value = name.value,
        onValueChange = {
            name.value = it
            userNameError = when {
                it.isEmpty() -> {
                    isNameAvailable.value = false
                    "이름을 입력해주세요."
                }
                !nameRegex.matches(name.value) -> {
                    isNameAvailable.value = false
                    "영문, 한글로만 10자 이내로 기재해주세요."
                }
                else -> {
                    isNameAvailable.value = true
                    null
                }
            }
        },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "이름",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        placeholder = { Text(text = "영문, 한글로만 10자 이내로 기재해주세요.") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MainBlue
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(top = 4.dp)
    )
    if (!userNameError.isNullOrEmpty()) {
        Text(text = userNameError!!, color = Color.Red)
    }
}