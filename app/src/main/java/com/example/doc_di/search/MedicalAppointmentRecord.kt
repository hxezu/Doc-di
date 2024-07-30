package com.example.doc_di.search

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.doc_di.etc.BtmBarViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MedicalAppointmentRecord(navController: NavController, btmBarViewModel: BtmBarViewModel) {
    val titleColor = Color(0xFF303437)
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) {
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
                    text = "\uD83C\uDFE5 검색 결과",
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
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(containerColor =  Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation =  2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column (
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "2023.12.27 이비인후과",
                                color = Color(0xFFBDBDBD),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "연세어울림이비인후과의원",
                                color = Color(0xFF333333),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "입원(외래)일 수 :  0(2)",
                                color = Color(0xFFBDBDBD),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                item {
                    Card(
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(containerColor =  Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation =  2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column (
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "2023.08.02 정형외과",
                                color = Color(0xFFBDBDBD),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "스타시티 정형외과 의원",
                                color = Color(0xFF333333),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "입원(외래)일 수 :  0(3)",
                                color = Color(0xFFBDBDBD),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}