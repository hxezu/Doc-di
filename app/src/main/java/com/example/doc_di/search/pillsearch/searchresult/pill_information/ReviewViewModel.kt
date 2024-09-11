package com.example.doc_di.search.pillsearch.searchresult.pill_information

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.review.ReviewData
import com.example.doc_di.etc.Routes
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {
    val showSearch = mutableStateListOf(true, false, false, false)

    private val _reviewList = MutableLiveData<List<ReviewData>?>()
    val reviewList: LiveData<List<ReviewData>?> get() = _reviewList

    fun fetchReviewInfo(
        context: Context,
        navController: NavController,
        userViewModel: UserViewModel,
        medicineName: String,
    ) {
        viewModelScope.launch {
            val reLogin = {
                navController.navigate(Routes.login.route) {
                    popUpTo(Routes.login.route) {
                        inclusive = true
                    }
                }
            }

            try {
                val accessToken = userViewModel.checkAccessAndReissue(context, navController)
                if (accessToken != null) {
                    val medicineResponse =
                        RetrofitInstance.reviewApi.findReview(accessToken, medicineName)
                    if (medicineResponse.code() == 200) {
                        _reviewList.postValue(medicineResponse.body()!!.data)
                    } else {
                        _reviewList.postValue(null)
                    }
                } else {
                    reLogin()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}