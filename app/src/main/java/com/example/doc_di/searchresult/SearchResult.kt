package com.example.doc_di.searchresult

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.search.SearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchResult(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    searchViewModel: SearchViewModel,
) {
    val titleColor = Color(0xFF303437)
    val cardPillTextColor = Color(0xFF262C3D)
    val reviewTextColor = Color(0xFF747F9E)
    val starColor = Color(0xFFFFC107)

    val pillList = searchViewModel.pills.collectAsState().value
    val isLoading = searchViewModel.isLoading.collectAsState().value

    LaunchedEffect(navController.currentBackStackEntry) {
        searchViewModel.resetPillInfo()
        searchViewModel.resetPills()
    }

    Scaffold(bottomBar = {
        BottomNavigationBar(
            navController = navController,
            btmBarViewModel = btmBarViewModel
        )
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp)
                .padding(top = 48.dp, bottom = 106.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "검색 결과",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .align(Alignment.Start)
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn() {
                    if (pillList.isEmpty()) {
                        item {
                            Text(text = "조회되는 경구약제가 없습니다.")
                        }
                    } else {
                        items(pillList) { pill ->
                            Card(
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .clickable {
                                        searchViewModel.setSelectedPill(pill)
                                        searchViewModel.setPillInfo(pill.itemName)
                                        navController.navigate(Routes.pillInformation.route) {
                                            popUpTo(Routes.searchMethod.route) { inclusive = false }
                                        }
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                ) {
                                    Text(
                                        text = pill.itemName,
                                        color = cardPillTextColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            tint = starColor,
                                            contentDescription = "별점",
                                        )
                                        Text(
                                            text = "4.5(834)",
                                            color = reviewTextColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(start = 4.dp, end = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}