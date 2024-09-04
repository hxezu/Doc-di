package com.example.doc_di.search.pillsearch.searchmethod.shapesearch

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color

@Composable
fun ShapeColorBtn(
    shapeOrColor: String,
    selectedShapeOrColor : MutableState<String>,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val needNull = if (shapeOrColor == "기타") "" else shapeOrColor
    val selectedButtonColor = Color.White
    val unselectedButtonColor = Color(0xFFF2F6F7)
    val selectedButtonTextColor = Color.Black
    val unselectedButtonTextColor = Color(0xFF616161)
    Button(
        onClick = {
            selectedShapeOrColor.value = needNull
            focusManager.clearFocus()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedShapeOrColor.value == needNull) selectedButtonColor else unselectedButtonColor
        ),
        modifier = modifier
    ) {
        Text(
            text = shapeOrColor,
            color = if (selectedShapeOrColor.value == needNull) selectedButtonTextColor else unselectedButtonTextColor
        )
    }
}