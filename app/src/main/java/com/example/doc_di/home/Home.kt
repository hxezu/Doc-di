package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController) {
    Scaffold(bottomBar = {BottomNavigationBar(navController = navController)}) {
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(start = 40.dp)
        )  {
            Image(
                painter = painterResource(id = R.drawable.user_image),
                contentDescription = "프로필",
                modifier = Modifier
                    .size(66.dp)
                    .clickable {
                    navController.navigate(Routes.profile.route)
                }
            )

            Text(
                text = "안녕하세요,\n김유정님 \uD83D\uDE00",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "다가오는 진료 일정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF404446)
            )
            // 카드 가로로 돌리기

            Text(
                text = "복용 알림",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF404446)
            )
            // 복용 알림
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    Home(navController = navController)
}