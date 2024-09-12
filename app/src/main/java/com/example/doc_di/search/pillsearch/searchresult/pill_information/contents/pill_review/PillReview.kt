package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.pill.RateInfo
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import kotlinx.coroutines.launch

@Composable
fun PillReview(
    selectedPill: Pill,
    reviewViewModel: ReviewViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    searchViewModel: SearchViewModel,
) {
    val reviewList by reviewViewModel.reviewList.observeAsState()

    val rateInfo = RateInfo(
        itemSeq = selectedPill.itemSeq.toString(),
        rateTotal =  reviewList?.sumOf { review -> review.rate.toInt() } ?: 0,
        rateAmount = reviewList?.size ?: 0,
    )

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(reviewList) {
        coroutineScope.launch {
            try {
                RetrofitInstance.pillApi.modifyRateInfo(rateInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    if (reviewList.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 106.dp)
        ) {
            Text(
                text = "기록된 리뷰가 없습니다.\n복용 후기를 작성해보세요!",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        reviewList?.let { revList ->
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 106.dp)
            ) {
                item {
                    ReviewStatistic(reviewList!!)
                }
                items(revList) { review ->
                    Review(review, navController, userViewModel, reviewViewModel, searchViewModel)
                }
            }
        }
    }
}