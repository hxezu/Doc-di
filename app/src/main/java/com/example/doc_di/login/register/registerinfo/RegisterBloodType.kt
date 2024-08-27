package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.doc_di.ui.theme.MainBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterBloodType(bloodType: MutableState<String>) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val bloodTypes = listOf("A형", "B형", "O형", "AB형")

    val focusManager = LocalFocusManager.current

    Box {
        OutlinedTextField(
            value = bloodType.value,
            onValueChange = { bloodType.value = it },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            label = {
                Text(
                    text = "혈액형",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            readOnly = true,
            placeholder = { Text(text = "혈액형 선택") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "혈액형 선택"
                    )
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MainBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
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
            bloodTypes.forEach { blood ->
                DropdownMenuItem(onClick = {
                    bloodType.value = blood
                    expanded = false
                    focusManager.clearFocus()
                }) {
                    Text(text = blood)
                }
            }
        }
    }
}
