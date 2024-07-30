package com.example.doc_di.search.searchmethod

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ShapeColorBtn(
    shapeOrColor: String,
    selectedShapeOrColor : MutableState<String>,
    modifier: Modifier = Modifier
) {
    val selectedButtonColor = Color.White
    val unselectedButtonColor = Color(0xFFF2F6F7)
    val selectedButtonTextColor = Color.Black
    val unselectedButtonTextColor = Color(0xFF616161)
    Button(
        onClick = { selectedShapeOrColor.value = shapeOrColor },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedShapeOrColor.value == shapeOrColor) selectedButtonColor else unselectedButtonColor
        ),
        modifier = modifier
    ) {
        Text(
            text = shapeOrColor,
            color = if (selectedShapeOrColor.value == shapeOrColor) selectedButtonTextColor else unselectedButtonTextColor
        )
    }
}