package com.example.doc_di.search.searchmethod

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.search.SearchViewModel

@Composable
fun ShapeSearch(navController: NavController, searchViewModel: SearchViewModel) {
    var prefixTextColor by remember { mutableStateOf(Color(0xFFC4CACF)) }
    var suffixTextColor by remember { mutableStateOf(Color(0xFFC4CACF)) }
    val textColor = Color(0xFF191D30)
    val unselectedButtonColor = Color(0xFFF2F6F7)

    var preIdentifier by remember { mutableStateOf("ex) TYER") }
    var sufIdentifier by remember { mutableStateOf("ex) 325") }
    val selectedShape = remember { mutableStateOf("타원형") }
    val selectedColor = remember { mutableStateOf("하양") }
    val mainSearchColor = Color(0xFF1892FA)

    val options = mutableMapOf<String, String>()
    val shapes = listOf("타원형", "원형", "장방형", "기타")
    val colors = listOf("하양", "노랑", "분홍", "초록", "파랑", "주황", "연두", "빨강", "기타")
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            }
    ) {
        androidx.compose.material.OutlinedTextField(
            value = preIdentifier,
            onValueChange = { preIdentifier = it },
            label = {
                Text(
                    text = "식별표시 앞",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191D30),
                )
            },
            textStyle = TextStyle(
                color = prefixTextColor
            ),
            colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF007AEB)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        prefixTextColor = Color.Black
                        preIdentifier = ""
                    }
                }
        )

        androidx.compose.material.OutlinedTextField(
            value = sufIdentifier,
            onValueChange = { sufIdentifier = it },
            label = {
                Text(
                    text = "식별표시 뒤",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191D30),
                )
            },
            textStyle = TextStyle(
                color = suffixTextColor
            ),
            colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF007AEB)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        sufIdentifier = ""
                        suffixTextColor = Color.Black
                    }
                }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "모양",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )
            Column(
                modifier = Modifier
                    .width(196.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(unselectedButtonColor)
            ) {
                shapes.chunked(2).forEach { row ->
                    Row {
                        row.forEach { shape ->
                            ShapeColorBtn(
                                shapeOrColor = shape,
                                selectedShapeOrColor = selectedShape,
                                focusManager = focusManager,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "색상",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Column(
                modifier = Modifier
                    .width(254.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(unselectedButtonColor)
            ) {
                colors.chunked(3).forEach { row ->
                    Row {
                        row.forEach { color ->
                            ShapeColorBtn(
                                shapeOrColor = color,
                                selectedShapeOrColor = selectedColor,
                                focusManager = focusManager,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                options["txt1"] = if (preIdentifier == "ex) TYER") "" else preIdentifier
                options["txt2"] = if (sufIdentifier == "ex) 325") "" else sufIdentifier
                options["shape"] = selectedShape.value
                options["color1"] = selectedColor.value
                searchViewModel.setOptions(options)
                searchViewModel.searchPillsByOptions()
                navController.navigate(Routes.searchResult.route)
            },
            colors = ButtonDefaults.textButtonColors(
                mainSearchColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "검색",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}