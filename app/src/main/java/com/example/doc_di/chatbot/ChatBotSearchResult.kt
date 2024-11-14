package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.clickableThrottleFirst
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.ShowPillList
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import com.example.doc_di.ui.theme.LightBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatBotSearchResult(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    searchViewModel: SearchViewModel,
    reviewViewModel: ReviewViewModel,
    reminderViewModel: ReminderViewModel
) {
    val titleColor = Color(0xFF303437)

    val pillList = searchViewModel.pills.collectAsState().value
    val isLoading = searchViewModel.isLoading.collectAsState().value

    LaunchedEffect(Unit) {
        userViewModel.userInfo.value?.email?.let { email ->
            reminderViewModel.getReminders(email)
        }
    }
    LaunchedEffect(pillList) {
        Log.d("ChatBotSearchResult", "Observed pillList update: $pillList")
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(top = 68.dp, bottom = 106.dp)
        ) {
            GoBack(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickableThrottleFirst { navController.popBackStack() }
            )
            Text(
                text = "검색 결과",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 28.dp)
                    .align(Alignment.Start)
            )

            if (isLoading) {
                CircularProgressIndicator(color = LightBlue)
            }
            else {
                if (pillList.isEmpty()) {
                    Text(text = "조회되는 경구약제가 없습니다.", color = Color.Black)
                } else {
                    ChatBotShowPillList(pillList, navController, userViewModel, searchViewModel, reviewViewModel)
                }
            }
        }
    }
}