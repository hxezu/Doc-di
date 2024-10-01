package com.example.doc_di.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
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
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 106.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            SearchGreeting(userViewModel)
            Spacer(modifier = Modifier.weight(1f))
            PillSearch(navController, searchViewModel)
            Spacer(modifier = Modifier.weight(2f))
        }
    }
}