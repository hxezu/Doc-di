package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
fun RegisterHeightWeight(
    height: MutableState<String>,
    weight: MutableState<String>,
    isHeightAvailable: MutableState<Boolean>,
    isWeightAvailable: MutableState<Boolean>,
) {

    var heightError by rememberSaveable { mutableStateOf<String?>(null) }
    val heightRegex = "^[0-9]{2,3}$".toRegex()

    var weightError by rememberSaveable { mutableStateOf<String?>(null) }
    val weightRegex = "^[0-9]{2,3}$".toRegex()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        OutlinedTextField(
            value = height.value,
            onValueChange = {
                height.value = it
                heightError = when {
                    it.isEmpty() -> {
                        isHeightAvailable.value = false
                        "키를 입력해주세요."
                    }
                    !heightRegex.matches(height.value) -> {
                        isHeightAvailable.value = false
                        "키를 두자리 혹은 세자리로 입력해주세요."
                    }
                    else -> {
                        isHeightAvailable.value = true
                        null
                    }
                }
            },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            label = {
                Text(
                    text = "키 (cm)",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            placeholder = { Text(text = "소수는 생략") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MainBlue
            ),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = weight.value,
            onValueChange = {
                weight.value = it
                weightError = when {
                    it.isEmpty() -> {
                        isWeightAvailable.value = false
                        "몸무게를 입력해주세요."
                    }
                    !weightRegex.matches(weight.value) -> {
                        isWeightAvailable.value = false
                        "몸무게를 두자리 혹은 세자리로 입력해주세요."
                    }
                    else -> {
                        isWeightAvailable.value = true
                        null
                    }
                }
            },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            label = {
                Text(
                    text = "몸무게 (kg)",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            placeholder = { Text(text = "소수는 생략") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MainBlue
            ),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
    }
    if (!heightError.isNullOrEmpty() && !weightError.isNullOrEmpty()) {
        Text(text = heightError!!, color = Color.Red)
        Text(text = weightError!!, color = Color.Red)
    } else if (!heightError.isNullOrEmpty()) {
        Text(text = heightError!!, color = Color.Red)
    } else if (!weightError.isNullOrEmpty()) {
        Text(text = weightError!!, color = Color.Red)
    }
}