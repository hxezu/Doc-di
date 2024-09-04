package com.example.doc_di.search.pillsearch.searchmethod.shapesearch

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.search.SearchViewModel

@Composable
fun ShapeSearch(navController: NavController, searchViewModel: SearchViewModel) {
    val preIdentifier = remember { mutableStateOf("") }
    val sufIdentifier = remember { mutableStateOf("") }
    val selectedShape = remember { mutableStateOf("타원형") }
    val selectedColor = remember { mutableStateOf("하양") }
    val mainSearchColor = Color(0xFF1892FA)

    val options = mutableMapOf<String, String>()
    val focusManager = LocalFocusManager.current

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
        EnterIdentificationMark(preIdentifier, sufIdentifier, focusManager)
        SelectShapeColor(selectedShape, selectedColor, focusManager)
        Button(
            onClick = {
                options["txt1"] = preIdentifier.value
                options["txt2"] = sufIdentifier.value
                options["shape"] = selectedShape.value
                options["color1"] = selectedColor.value
                searchViewModel.setOptions(options)
                searchViewModel.searchPillsByOptions()
                navController.navigate(Routes.searchResult.route)
            },
            colors = ButtonDefaults.textButtonColors(mainSearchColor),
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