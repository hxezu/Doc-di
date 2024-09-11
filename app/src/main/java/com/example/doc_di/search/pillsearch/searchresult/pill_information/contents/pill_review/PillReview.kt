package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel

@Composable
fun PillReview(
    selectedPill: Pill,
    reviewViewModel: ReviewViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    searchViewModel: SearchViewModel,
) {
    val reviewList by reviewViewModel.reviewList.observeAsState()
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
                    ReviewStatistic(selectedPill, reviewList!!)
                }
                items(revList) { review ->
                    Review(review, navController, userViewModel, reviewViewModel, searchViewModel)
                }
            }
        }
    }
}