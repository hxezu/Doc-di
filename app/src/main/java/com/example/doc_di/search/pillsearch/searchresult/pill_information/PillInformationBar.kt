package com.example.doc_di.search.pillsearch.searchresult.pill_information

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PillInformationBar(reviewViewModel: ReviewViewModel) {
    val buttonColor = Color(0xFF007AEB)

    val searchTitle = arrayOf("정보", "용법", "주의사항", "효능통계")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(4) { index ->
            Button(
                onClick = {
                    for (j in 0 until reviewViewModel.showSearch.size) {
                        reviewViewModel.showSearch[j] = index == j
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    if (reviewViewModel.showSearch[index]) buttonColor else Color.Transparent
                ),
                border = if (!reviewViewModel.showSearch[index]) BorderStroke(
                    1.dp,
                    Color.LightGray
                )
                else null,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(48.dp)
                    .width(100.dp)
            ) {
                Text(
                    text = searchTitle[index],
                    color = if (reviewViewModel.showSearch[index]) Color.White else Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = if (reviewViewModel.showSearch[index]) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}