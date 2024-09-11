package com.example.doc_di.search.pillsearch.searchresult.pill_information

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.doc_di.UserViewModel
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.GoBack
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.PillInfo
import com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.PillUsage
import com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.PillWarning
import com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review.PillReview
import com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review.PillReviewDialog
import com.example.doc_di.ui.theme.LightBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PillInformation(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    searchViewModel: SearchViewModel,
    userViewModel: UserViewModel,
    reviewViewModel: ReviewViewModel,
) {
    val titleColor = Color(0xFF303437)
    val buttonColor = Color(0xFF007AEB)

    val isLoading = searchViewModel.isLoading.collectAsState().value
    val selectedPill = searchViewModel.getSelectedPill()
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(selectedPill.itemImage)
            .size(Size.ORIGINAL)
            .build()
    ).state

    var showPillReviewDialog by rememberSaveable { mutableStateOf(false) }
    if (showPillReviewDialog) {
        PillReviewDialog(
            onDismiss = { showPillReviewDialog = false },
            navController = navController,
            searchViewModel = searchViewModel,
            userViewModel = userViewModel,
            reviewViewModel = reviewViewModel
        )
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent,
        /* TODO 사용자가 복용중인 약 리스트안에 search result의 약이 있다면 효능통계 갔을 시 + Floating Button */
        floatingActionButton = {
            if (reviewViewModel.showSearch[3]) {
                FloatingActionButton(
                    onClick = { showPillReviewDialog = true },
                    containerColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "후기 작성 버튼",
                        tint = buttonColor
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(top = 68.dp)
        ) {
            GoBack(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = selectedPill.itemName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            if (imageState is AsyncImagePainter.State.Error || isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(246.dp, 132.dp)
                ) {
                    CircularProgressIndicator(color = LightBlue)
                }
            } else if (imageState is AsyncImagePainter.State.Success) {
                Image(
                    painter = imageState.painter,
                    contentDescription = "검색 결과 약 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(246.dp, 132.dp)
                )
            }
            PillInformationBar(reviewViewModel)
            when {
                reviewViewModel.showSearch[0] -> PillInfo(searchViewModel)
                reviewViewModel.showSearch[1] -> PillUsage(searchViewModel)
                reviewViewModel.showSearch[2] -> PillWarning(searchViewModel)
                reviewViewModel.showSearch[3] -> PillReview(
                    selectedPill,
                    reviewViewModel,
                    navController,
                    userViewModel,
                    searchViewModel
                )
            }
        }
    }
}