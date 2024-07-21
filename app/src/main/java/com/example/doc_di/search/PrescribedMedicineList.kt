package com.example.doc_di.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun PrescribedMedicineList(navController: NavController) {
    // 실제로는 약 리스트 받아와서  선택 한 것에 대해서만 true를 만들고 해당 것만 보일 것
    val headlineColor = Color(0xFF404446)
    val titleColor = Color(0xFF303437)
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(vertical = 48.dp)
        ) {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterStart)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = "2023년 12월 27일",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "이(e)누리 내과 의원",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "건대 약국",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF72777A)
            )
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 106.dp)
            ){
                item {
                    Card(
                        onClick = { navController.navigate(Routes.pillInformation.route) },
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Column (
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "제산제",
                                    color = Color.LightGray,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "알마겔정",
                                    color = Color(0xFF333333),
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "1일 3회 1정씩 5일간",
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
                    }
                }

                item {
                    Card(
                        onClick = { navController.navigate(Routes.pillInformation.route) },
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Column (
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "해열.진통.소염제",
                                    color = Color.LightGray,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "록프라정",
                                    color = Color(0xFF333333),
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "1일 3회 1정씩 5일간",
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
                    }
                }

                item {
                    Card(
                        onClick = { navController.navigate(Routes.pillInformation.route) },
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Column (
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "주로 그람 양성, 음성 균에 작용하는것",
                                    color = Color.LightGray,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "알보젠세파클러수화물",
                                    color = Color(0xFF333333),
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "1일 3회 1캡슐씩 5일간",
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
                    }
                }
            }
        }
    }
}