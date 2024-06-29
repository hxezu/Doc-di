package com.example.doc_di.searchresult

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchResultScreen(navController: NavController) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Text(text ="알약 검색 결과 화면")
    }
}