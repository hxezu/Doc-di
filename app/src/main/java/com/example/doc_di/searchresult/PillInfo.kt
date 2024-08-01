package com.example.doc_di.searchresult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.search.SearchViewModel

@Composable
fun PillInfo(searchViewModel:SearchViewModel) {
    val selectedPill = searchViewModel.getSelectedPill()
    val selectedPillInfo = searchViewModel.pillInfo.collectAsState().value

    val cardTitleColor = Color(0xFF333333)
    val cardDetailTextColor = Color(0xFF747F9E)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 106.dp)
    ) {
        item {
            Card(
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "외형정보", color = cardTitleColor, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "성상 : ${if (selectedPill.chart != "") selectedPill.chart else "-"}",
                        color = cardDetailTextColor,
                        lineHeight = 14.sp,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "제형 : ${
                            when (selectedPill.formCodeName) {
                                "" -> "-"
                                "나정" -> "정제"
                                else -> selectedPill.formCodeName
                            }
                        }",
                        color = cardDetailTextColor,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "모양 : ${if (selectedPill.drugShape != "") selectedPill.drugShape else "-"}",
                        color = cardDetailTextColor,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "색상 : ${if (selectedPill.colorClass1 != "") selectedPill.colorClass1 else "-"}, " +
                                if (selectedPill.colorClass2 != "") selectedPill.colorClass2 else "-",
                        color = cardDetailTextColor,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "분할선 : (앞) ${if (selectedPill.lineFront != "") "있음" else "없음"}, " +
                                "(뒤) ${if (selectedPill.lineBack != "") "있음" else "없음"}",
                        color = cardDetailTextColor,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "식별표기 : (앞) ${if (selectedPill.printFront != "") selectedPill.printFront else "-"}, " +
                                "(뒤) ${if (selectedPill.printBack !="") selectedPill.printBack else "-"}",
                        color = cardDetailTextColor,
                        fontSize = 10.sp
                    )
                }
            }
        }

        item {
            Card(
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "효능", color = cardTitleColor, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedPillInfo.efcyQesitm != "") selectedPillInfo.efcyQesitm else "- 정보 미제공 -",
                        color = cardDetailTextColor,
                        lineHeight = 14.sp,
                        fontSize = 10.sp
                    )
                }
            }
        }

        item {
            Card(
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "보관 방법", color = cardTitleColor, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedPillInfo.depositMethodQesitm != "") selectedPillInfo.depositMethodQesitm else "- 정보 미제공 -",
                        color = cardDetailTextColor,
                        lineHeight = 14.sp,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

