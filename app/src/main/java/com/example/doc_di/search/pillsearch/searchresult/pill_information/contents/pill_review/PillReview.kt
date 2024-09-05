package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PillReview() {
    val starColor = Color(0xFFFFC000)
    val statisticNameColor = Color(0xFF090F47)
    val barGraphColor = Color(0xFF4157FF)
    val statisticTextColor = Color.Gray
    val barBackgroundColor = Color.LightGray
    val reviewPercentage = listOf(
        5 to 67,
        4 to 20,
        3 to 7,
        2 to 0,
        1 to 1
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 106.dp)
    ) {
        item {
            Row() {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "4.4",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Sharp.Star,
                            contentDescription = "전체 평점 별",
                            tint = starColor,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "평점 923 개",
                        fontSize = 15.sp,
                        color = statisticTextColor
                    )
                    Text(
                        text = "리뷰 257 개",
                        fontSize = 15.sp,
                        color = statisticTextColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(start = 12.dp, top = 4.dp)
                        .width(1.dp)
                        .height(120.dp)
                        .background(Color.LightGray)

                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    reviewPercentage.forEach { (stars, percentage) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "$stars",
                                color = statisticTextColor,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(1.dp))
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "통계 별",
                                tint = starColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            LinearProgressIndicator(
                                progress = percentage / 100f,
                                color = barGraphColor,
                                trackColor = barBackgroundColor,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (percentage / 10 == 0) "$percentage%  "
                                else "$percentage%",
                                fontSize = 14.sp,
                                color = statisticTextColor
                            )
                        }
                    }
                }
            }
        }


        item {
            Card(
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "리뷰 별",
                            tint = starColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "4.2", color = statisticTextColor, fontSize = 13.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "2020-10-05",
                            color = statisticTextColor,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "이 * 우", color = statisticNameColor, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "개인적으로는 두통 등의 통증에 대해서 타이레놀이 잘 드는 것 같습니다. 그래서 두통이 느껴질 때 자연스럽게 찾게 되는 것 같습니다.",
                        color = statisticTextColor,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
        item {
            Card(
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "리뷰 별",
                            tint = starColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "4.2", color = statisticTextColor, fontSize = 13.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "2021-03-23",
                            color = statisticTextColor,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "김 * 영", color = statisticNameColor, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "타이레놀 160mg 효과 생각보다 나은듯 해요.\n먹고난후 상태가 이정도이면 굿잡 !",
                        color = statisticTextColor,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}