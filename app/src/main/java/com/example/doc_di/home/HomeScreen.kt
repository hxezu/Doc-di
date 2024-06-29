package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.etc.BottomNavigationBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(bottomBar = {BottomNavigationBar(navController = navController)}) {
        Text(text = "메인 화면")
    }
}

@Preview(showBackground = true)
@Composable
fun aasd() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}