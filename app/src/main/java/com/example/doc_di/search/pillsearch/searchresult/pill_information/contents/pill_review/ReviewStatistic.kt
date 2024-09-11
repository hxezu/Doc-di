package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.domain.review.ReviewData

@Composable
fun ReviewStatistic(reviewList: List<ReviewData>) {
    val starColor = Color(0xFFFFC000)
    val barGraphColor = Color(0xFF4157FF)
    val statisticTextColor = Color.Gray
    val barBackgroundColor = Color.LightGray

    val ratingCount = mutableMapOf(
        5 to 0,
        4 to 0,
        3 to 0,
        2 to 0,
        1 to 0
    )

    for (review in reviewList) {
        val rate = review.rate.toInt()
        ratingCount[rate] = ratingCount[rate]!! + 1
    }

    val totalReviewNum = reviewList.size

    val reviewPercentage = ratingCount.mapValues { (rate, count) ->
        count * 100 / totalReviewNum.toDouble()
    }

    val averageRate = reviewList.sumOf { it.rate.toInt() } / totalReviewNum.toDouble()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(144.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = String.format("%.1f", averageRate),
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "평점 ${reviewList.size} 개",
                fontSize = 15.sp,
                color = statisticTextColor
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .padding(start = 24.dp, end = 12.dp)
                .width(1.dp)
                .height(132.dp)
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
                        fontSize = 14.sp,
                        modifier = Modifier.width(8.dp)
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "통계 별",
                        tint = starColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = { percentage.toFloat() / 100f },
                        color = barGraphColor,
                        trackColor = barBackgroundColor,
                        modifier = Modifier.width(100.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${String.format("%.1f", percentage)}%",
                        fontSize = 14.sp,
                        color = statisticTextColor
                    )
                }
            }
        }
    }
}
