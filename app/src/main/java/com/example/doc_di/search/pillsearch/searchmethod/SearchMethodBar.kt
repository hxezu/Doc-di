package com.example.doc_di.search.pillsearch.searchmethod

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.search.SearchViewModel

@Composable
fun SearchMethodBar(searchViewModel: SearchViewModel) {
    val howSearchButtonColor = Color(0xFF007AEB)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until searchViewModel.showSearch.size) {
            Button(
                onClick = {
                    for (j in 0 until searchViewModel.showSearch.size) {
                        searchViewModel.showSearch[j] = i == j
                    }
                },
                border = if (!searchViewModel.showSearch[i]) BorderStroke(
                    1.dp,
                    Color.LightGray
                )
                else null,
                colors = if (searchViewModel.showSearch[i]) ButtonDefaults.textButtonColors(
                    howSearchButtonColor
                )
                else ButtonDefaults.textButtonColors(Color.Transparent),
                modifier = Modifier.size(width = 124.dp, height = 48.dp)
            ) {
                Text(
                    text = searchViewModel.searchTitle[i],
                    color = if (searchViewModel.showSearch[i]) Color.White else Color.LightGray,
                    fontSize = 14.sp,
                    fontWeight = if (searchViewModel.showSearch[i]) FontWeight.Bold else null
                )
            }
        }
    }
}