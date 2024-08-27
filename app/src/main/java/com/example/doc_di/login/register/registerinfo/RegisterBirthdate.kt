package com.example.doc_di.login.register.registerinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.doc_di.R
import com.example.doc_di.ui.theme.MainBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterBirthdate(birthDate: MutableState<String>) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = birthDate.value,
        onValueChange = { birthDate.value = it },
        shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
        label = {
            Text(
                "생년월일",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        readOnly = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "날짜 선택",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { showDatePicker = true }
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                if (isFocused) showDatePicker = true
            },
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
                focusManager.clearFocus()
            },
            confirmButton = { },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp)
        ) {
            val datePickerState = rememberDatePickerState(
                yearRange = 1900..2024,
                initialDisplayMode = DisplayMode.Picker,
            )
            DatePicker(state = datePickerState)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    showDatePicker = false
                    focusManager.clearFocus()
                }) {
                    Text(text = "취소")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            val yyyyMMdd = SimpleDateFormat(
                                "yyyyMMdd",
                                Locale.getDefault()
                            ).format(Date(selectedDateMillis))
                            birthDate.value = yyyyMMdd
                        }
                        showDatePicker = false
                        focusManager.clearFocus()
                    }
                ) {
                    Text(text = "확인")
                }
            }
        }
    }
}