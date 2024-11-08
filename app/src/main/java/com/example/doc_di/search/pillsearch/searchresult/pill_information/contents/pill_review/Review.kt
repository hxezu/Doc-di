package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.domain.review.ReviewData
import com.example.doc_di.domain.review.ReviewImpl
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import kotlinx.coroutines.launch

@Composable
fun Review(
    review: ReviewData,
    navController: NavController,
    userViewModel: UserViewModel,
    reviewViewModel: ReviewViewModel,
    searchViewModel: SearchViewModel,
) {
    val starColor = Color(0xFFFFC000)
    val statisticNameColor = Color(0xFF090F47)
    val statisticTextColor = Color.Gray

    var showEditPillReviewDialog by rememberSaveable { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val reviewImpl = ReviewImpl()

    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = review.name,
                    color = statisticNameColor,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "리뷰 별",
                        tint = starColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = review.rate.toString(), color = starColor, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = review.date,
                        color = statisticTextColor,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (review.email == userViewModel.userInfo.value!!.email)
                                expanded = true
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "리뷰 메뉴",
                        tint = statisticTextColor
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(120.dp)
                            .background(Color.White)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                showEditPillReviewDialog = true
                            },
                        ) {
                            Text(text = "수정")
                        }
                        DropdownMenuItem(
                            onClick = {
                                if (isNetworkAvailable(context)) {
                                    expanded = false
                                    scope.launch {
                                        reviewImpl.deleteReview(
                                            review,
                                            context,
                                            navController,
                                            userViewModel,
                                            reviewViewModel
                                        )
                                    }
                                }
                                else {
                                    Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                                }
                            },
                        ) {
                            Text(text = "삭제")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.statistic,
                color = statisticTextColor,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
        }
    }

    if (showEditPillReviewDialog) {
        EditPillReviewDialog(
            onDismiss = { showEditPillReviewDialog = false },
            review = review,
            navController = navController,
            reviewViewModel = reviewViewModel,
            userViewModel = userViewModel,
            searchViewModel = searchViewModel
        )
    }
}