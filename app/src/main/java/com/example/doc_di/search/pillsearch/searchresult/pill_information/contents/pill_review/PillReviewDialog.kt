package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.doc_di.domain.review.ReviewImpl
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PillReviewDialog(
    onDismiss: () -> Unit,
    navController: NavController,
    searchViewModel: SearchViewModel,
    userViewModel: UserViewModel,
    reviewViewModel: ReviewViewModel,
) {
    val starYellow = Color(0xFFFFC107)
    val starGray = Color(0xFFE9EBED)
    val buttonColor = Color(0xFF4B7BE5)

    var reviewText = rememberSaveable { mutableStateOf("") }
    var curStarRating by rememberSaveable { mutableStateOf<Short>(0) }

    val selectedPill = searchViewModel.getSelectedPill()
    val userInfo by userViewModel.userInfo.observeAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val reviewImpl = ReviewImpl()

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 40.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() }
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        text = "후기 작성",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.Center) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "평점",
                            tint = if (curStarRating >= i.toShort()) starYellow else starGray,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { curStarRating = i.toShort() }
                        )
                        if (i in 1..4) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                DialogDetailReview(reviewText)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (isNetworkAvailable(context)) {
                            scope.launch {
                                reviewImpl.createReview(
                                    userInfo,
                                    selectedPill,
                                    reviewText.value,
                                    curStarRating,
                                    context,
                                    navController,
                                    userViewModel,
                                    reviewViewModel,
                                    onDismiss
                                )
                            }
                        }
                        else {
                            Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "작성 완료", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}