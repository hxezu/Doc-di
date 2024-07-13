package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController) {
    val greetTextColor = Color(0xFF303437)
    val titleColor = Color(0xFF404446)
    val cardPillColor = Color(0xFF202325)
    val alarmColor = Color(0xFF979C9E)
    val pinColor = Color(0xFF979C9E)
    val starColor = Color(0xFFFFC462)
    val cardTextColor = Color(0xFF72777A)


    Scaffold(bottomBar = {BottomNavigationBar(navController = navController)}) {
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, bottom = 106.dp)
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
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = greetTextColor
            )

            Text(
                text = "다가오는 진료 일정",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            // 카드 가로로 돌리기

            Column {
                Text(
                    text = "복용 알림",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    item {
                        Card(
                            onClick = { /* TODO{복용알림의 약 수정 화면으로}*/ },
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(1.dp),
                            modifier = Modifier
                                .width(180.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "타이레놀",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = cardPillColor
                                )
                                Spacer(modifier = Modifier.size(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.alarm),
                                        contentDescription = "복용 알람 아이콘",
                                        tint = alarmColor,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "10:00 AM",
                                        color = cardTextColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pin),
                                        contentDescription = "핀 아이콘",
                                        tint = pinColor,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "식후",
                                        color = cardTextColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = "평점 아이콘",
                                        tint = starColor,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "5.0",
                                        color = cardTextColor
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            onClick = { /* TODO{복용알림의 약 수정 화면으로}*/ },
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(1.dp),
                            modifier = Modifier
                                .width(180.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "오메가 3",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = cardPillColor
                                )
                                Spacer(modifier = Modifier.size(12.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.alarm),
                                        contentDescription = "복용 알람 아이콘",
                                        tint = alarmColor,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "08:00 PM",
                                        color = cardTextColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pin),
                                        contentDescription = "핀 아이콘",
                                        tint = pinColor,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "식전",
                                        color = cardTextColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = "평점 아이콘",
                                        tint = starColor,
                                        modifier = Modifier
                                            .size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "5.0",
                                        color = cardTextColor
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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    Home(navController = navController)
}