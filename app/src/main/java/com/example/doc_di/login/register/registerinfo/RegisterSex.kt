package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun RegisterSex(sex: MutableState<String>) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val genderOptions = listOf("남성", "여성")

    val focusManager = LocalFocusManager.current

    Box {
        OutlinedTextField(
            value = sex.value,
            onValueChange = { sex.value = it },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            label = {
                Text(
                    text = "성별",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            placeholder = { Text(text = "성별 선택")},
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "성별 선택"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        expanded = true
                    }
                }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            },
            modifier = Modifier.fillMaxWidth(0.3f)
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    onClick = {
                        sex.value = gender
                        expanded = false
                        focusManager.clearFocus()
                    },
                ) {
                    Text(text = gender)
                }
            }
        }
    }
}
