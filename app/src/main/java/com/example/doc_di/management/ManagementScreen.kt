package com.example.doc_di.management

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManagementScreen(navController: NavController) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Text(text = "혜주/복용 관리 화면")
    }
}