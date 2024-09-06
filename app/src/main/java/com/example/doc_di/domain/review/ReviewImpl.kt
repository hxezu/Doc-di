package com.example.doc_di.domain.review

import android.content.Context
import androidx.navigation.NavController
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountDTO
import com.example.doc_di.domain.model.Pill
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
        onDismiss: () -> Unit
    ) {
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
            val reviewResponse = RetrofitInstance.reviewApi.createReview(accessToken!!, reviewDTO)
            if (reviewResponse.isSuccessful) {
                onDismiss()
            }
        }
    }
}