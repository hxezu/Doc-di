package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController, btmBarViewModel: BtmBarViewModel) {
    val greetTextColor = Color(0xFF303437)
    val titleColor = Color(0xFF404446)
    val cardPillColor = Color(0xFF202325)
    val cardProfessorColor = Color(0xFF202325)
    val treatmentTimeColor = Color(0xFF404446)
    val locateColor = Color(0xFF6C7072)
    val departmentColor = Color(0xFF5555CB)
    val alarmColor = Color(0xFF979C9E)
    val pinColor = Color(0xFF979C9E)
    val starColor = Color(0xFFFFC462)
    val cardTextColor = Color(0xFF72777A)


    Scaffold(bottomBar = {BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)}) {
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

            Column {
                Text(
                    text = "다가오는 진료 일정",
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
                        Box(
                            modifier = Modifier
                                .width(280.dp)
                        ) {
                            Card(
                                onClick = {navController.navigate(Routes.appointmentSchedule.route)},
                                colors = CardDefaults.cardColors(Color(0xFFF0F0FF)),
                                elevation = CardDefaults.cardElevation(1.dp),
                                modifier = Modifier
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp, vertical = 20.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "조종호 교수",
                                            color = cardProfessorColor,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "오늘 오후 2:45",
                                            color = treatmentTimeColor,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.height(72.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = "위치 아이콘",
                                                tint = locateColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "강남 세브란스 병원",
                                                color = locateColor,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 70.dp, height = 26.dp)
                                                .clip(shape = RoundedCornerShape(48.dp))
                                                .background(Color.White)
                                        ) {
                                            Text(
                                                text = "외과",
                                                color = departmentColor,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.doctor_image),
                                contentDescription = "의사 이미지",
                                modifier = Modifier
                                    .size(160.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 4.dp, y = 36.dp)
                            )
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .width(280.dp)
                        ) {
                            Card(
                                onClick = { navController.navigate(Routes.appointmentSchedule.route) },
                                colors = CardDefaults.cardColors(Color(0xFFFFF9F0)),
                                elevation = CardDefaults.cardElevation(1.dp),
                                modifier = Modifier
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp, vertical = 20.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "문일준 교수",
                                            color = cardProfessorColor,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "내일 오전 08:00 AM",
                                            color = treatmentTimeColor,
                                            fontSize = 12.sp
                                        )
                                        Spacer(modifier = Modifier.height(72.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = "위치 아이콘",
                                                tint = locateColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "강남 세브란스",
                                                color = locateColor,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 70.dp, height = 26.dp)
                                                .clip(shape = RoundedCornerShape(48.dp))
                                                .background(Color.White)
                                        ) {
                                            Text(
                                                text = "WARM-UP",
                                                color = Color(0xFFA05E03),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                            Image(
                                painter = painterResource(id = R.drawable.warm_up_image),
                                contentDescription = "의사 이미지",
                                modifier = Modifier
                                    .size(140.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 4.dp, y = 20.dp)
                            )
                        }
                    }
                }
            }

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
                            onClick = {
                                btmBarViewModel.btmNavBarItems[0].selected = false
                                btmBarViewModel.btmNavBarItems[1].selected = true
                                navController.navigate(Routes.pillInformation.route)
                            },
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
                                        text = "주로 그람양...",
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
                            onClick = {
                                btmBarViewModel.btmNavBarItems[0].selected = false
                                btmBarViewModel.btmNavBarItems[1].selected = true
                                navController.navigate(Routes.pillInformation.route)
                            },
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
                                        text = "해열제",
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
    val btmBarViewModel: BtmBarViewModel = viewModel()
    Home(navController = navController, btmBarViewModel = btmBarViewModel)
}