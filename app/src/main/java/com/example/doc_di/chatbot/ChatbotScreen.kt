package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatbotScreen(navController: NavController) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Text("혜주/챗봇 리스트 화면")
    }
}