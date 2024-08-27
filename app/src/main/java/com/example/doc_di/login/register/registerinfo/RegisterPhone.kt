package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.layout.fillMaxWidth
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
fun RegisterPhone(phoneNumber: MutableState<String>, isPhoneNumberAvailable: MutableState<Boolean>) {

    var phoneNumberError by rememberSaveable { mutableStateOf<String?>(null) }
    val phoneNumberRegex = "^[0-9]{11}$".toRegex()

    OutlinedTextField(
        value = phoneNumber.value,
        onValueChange = {
            phoneNumber.value = it
            phoneNumberError = when {
                it.isEmpty() -> {
                    isPhoneNumberAvailable.value = false
                    "전화번호를 입력해주세요."
                }
                !phoneNumberRegex.matches(phoneNumber.value) -> {
                    isPhoneNumberAvailable.value = false
                    "11자리 숫자로 기재해주세요."
                }
                else -> {
                    isPhoneNumberAvailable.value = true
                    null
                }
            }
        },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                text = "전화번호",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        placeholder = { Text(text = "'-'없이 입력' ex)01012345678") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Phone
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MainBlue
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    if (!phoneNumberError.isNullOrEmpty()){
        Text(text = phoneNumberError!!, color = Color.Red)
    }
}