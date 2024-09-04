package com.example.doc_di.search.pillsearch.searchmethod.shapesearch

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.example.doc_di.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterIdentificationMark(
    preIdentifier: MutableState<String>,
    sufIdentifier: MutableState<String>,
    focusManager: FocusManager,
) {
    var prefixTextColor by remember { mutableStateOf(Color(0xFFC4CACF)) }
    var suffixTextColor by remember { mutableStateOf(Color(0xFFC4CACF)) }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = preIdentifier.value,
        onValueChange = { preIdentifier.value = it },
        label = {
            Text(
                text = "식별표시 앞",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191D30),
            )
        },
        placeholder = { Text(text = "ex) TYER", color = Color(0xFF191D30)) },
        textStyle = TextStyle(color = prefixTextColor),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = LightBlue,
            cursorColor = Color(0xFF007AEB),
            focusedBorderColor = Color(0xFF007AEB)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    prefixTextColor = Color.Black
                    preIdentifier.value = ""
                }
            }
    )

    OutlinedTextField(
        value = sufIdentifier.value,
        onValueChange = { sufIdentifier.value = it },
        label = {
            Text(
                text = "식별표시 뒤",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF191D30),
            )
        },
        placeholder = { Text(text = "ex) 325", color = Color(0xFF191D30)) },
        textStyle = TextStyle(color = suffixTextColor),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = LightBlue,
            cursorColor = Color(0xFF007AEB),
            focusedBorderColor = Color(0xFF007AEB)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    sufIdentifier.value = ""
                    suffixTextColor = Color.Black
                }
            }
    )
}