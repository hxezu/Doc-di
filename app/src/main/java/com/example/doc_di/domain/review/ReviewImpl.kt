package com.example.doc_di.domain.review

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountDTO
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ReviewImpl {
    suspend fun createReview(
        userInfo: AccountDTO?,
        selectedPill: Pill,
        reviewText: String,
        curStarRating: Short,
        context: Context,
        navController: NavController,
        userViewModel: UserViewModel,
        reviewViewModel: ReviewViewModel,
        onDismiss: () -> Unit,
    ) {
        if (reviewText.isNotEmpty() && curStarRating.toInt() != 0) {
            if (userInfo != null) {
                val accessToken = userViewModel.checkAccessAndReissue(context, navController)
                val currentDate = LocalDate.now().toString()
                val reviewDTO = ReviewDTO(
                    email = userInfo.email,
                    name = userInfo.name,
                    medicineName = selectedPill.itemName,
                    statistic = reviewText,
                    date = currentDate,
                    rate = curStarRating
                )
                val reviewResponse =
                    RetrofitInstance.reviewApi.createReview(accessToken!!, reviewDTO)


                if (reviewResponse.isSuccessful) {
                    reviewViewModel.fetchReviewInfo(
                        context,
                        navController,
                        userViewModel,
                        selectedPill.itemName
                    )
                    onDismiss()
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "별점과 리뷰를 작성해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}