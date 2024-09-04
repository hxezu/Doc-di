package com.example.doc_di.search.pillsearch.searchmethod.shapesearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectShapeColor(
    selectedShape: MutableState<String>,
    selectedColor: MutableState<String>,
    focusManager: FocusManager,
) {
    val textColor = Color(0xFF191D30)
    val unselectedButtonColor = Color(0xFFF2F6F7)

    val shapes = listOf("타원형", "원형", "장방형", "기타")
    val colors = listOf("하양", "노랑", "분홍", "초록", "파랑", "주황", "연두", "빨강", "기타")

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
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
}