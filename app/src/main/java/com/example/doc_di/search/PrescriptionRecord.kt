package com.example.doc_di.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PrescriptionRecord(navController: NavController) {
    val titleColor = Color(0xFF303437)
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(vertical = 48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\uD83D\uDC8A 처방 기록 조회",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "정렬 아이콘",
                    modifier = Modifier.size(20.dp)
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 106.dp)
            ){
                item {
                    Card(
                        onClick = { navController.navigate(Routes.prescribedMedicineList.route) },
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.red_pill),
                                    contentDescription = "구별 아이콘(빨강)",
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.fillMaxHeight()) {
                                    Text(
                                        text = "이(e)누리 내과 의원",
                                        color = Color(0xFF333333),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "건대 약국",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowRight,
                                    contentDescription = "",
                                    tint = Color(0xFF191D30)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Card (
                                colors = CardDefaults.cardColors(Color(0xFFF5F5F5)),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.size(112.dp,32.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.date),
                                        contentDescription = "날짜",
                                        tint = titleColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "2023.12.27",
                                        fontWeight = FontWeight.SemiBold,
                                        color = titleColor,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        onClick = { navController.navigate(Routes.prescribedMedicineList.route) },
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.blue_pill),
                                    contentDescription = "구별 아이콘(빨강)",
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.fillMaxHeight()) {
                                    Text(
                                        text = "24시 열린 의원",
                                        color = Color(0xFF333333),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "초록 약국",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowRight,
                                    contentDescription = "",
                                    tint = Color(0xFF191D30)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Card (
                                colors = CardDefaults.cardColors(Color(0xFFF5F5F5)),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.size(112.dp,32.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.date),
                                        contentDescription = "날짜",
                                        tint = titleColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "2023.08.02",
                                        fontWeight = FontWeight.SemiBold,
                                        color = titleColor,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        onClick = { navController.navigate(Routes.prescribedMedicineList.route) },
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.red_pill),
                                    contentDescription = "구별 아이콘(빨강)",
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.fillMaxHeight()) {
                                    Text(
                                        text = "별 이비인후과 의원",
                                        color = Color(0xFF333333),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "햇살 온누리 약국",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowRight,
                                    contentDescription = "",
                                    tint = Color(0xFF191D30)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Card (
                                colors = CardDefaults.cardColors(Color(0xFFF5F5F5)),
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier.size(112.dp,32.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.date),
                                        contentDescription = "날짜",
                                        tint = titleColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "2023.06.11",
                                        fontWeight = FontWeight.SemiBold,
                                        color = titleColor,
                                        fontSize = 13.sp
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