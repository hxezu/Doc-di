package com.example.doc_di.login.register

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text("회원가입") },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(Routes.login.route) {
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
        }
    )
}