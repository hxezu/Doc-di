package com.example.doc_di.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.search.pillsearch.PillSearch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Search(
    navController: NavController,
    searchViewModel: SearchViewModel,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
) {
    val pastHistory by userViewModel.pastHistory.observeAsState()
    val context = LocalContext.current
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 106.dp)
        ) {
            SearchGreeting(userViewModel)
            PillSearch(navController, searchViewModel)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp)
            ) {
                Text(
                    text = "최근 알약 검색 기록",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                )
                PastSearchList(pastHistory, context, navController, userViewModel, searchViewModel)
            }
        }
    }
}