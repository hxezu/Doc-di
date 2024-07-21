package com.example.doc_di.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppointmentSchedule(navController: NavController) {
    var reservedTreatment by remember { mutableStateOf(true) }
   // var pastTreatment by remember { mutableStateOf(false) }

    var optionsExpanded by remember{ mutableStateOf(false)}
    val options = listOf<String>("삭제", "수정")
    var selectedOption by remember { mutableStateOf("")}

    var optionsExpanded1 by remember{ mutableStateOf(false)}
    val options1 = listOf<String>("삭제", "수정")
    var selectedOption1 by remember { mutableStateOf("")}

    val dateColor = Color(0xFF191D30)
    val textColor = Color(0xFF333333)
    val dateBackgroundColor = Color(0xFFF5F5F5)

    val reservedCardColor = if(reservedTreatment) Color.White else Color(0xFFF6F7FA)
    val reservedCardTextColor = if(reservedTreatment) Color(0xFF242B42) else Color(0xFF1D1E25)
    val reservedCardTextFontWeight = if(reservedTreatment) FontWeight.Bold else FontWeight.Normal
    val reservedCardElevation = if(reservedTreatment) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(0.dp)

    val pastCardColor = if(!reservedTreatment) Color.White else Color(0xFFF6F7FA)
    val pastCardTextColor = if(!reservedTreatment) Color(0xFF242B42) else Color(0xFF1D1E25)
    val pastCardTextFontWeight = if(!reservedTreatment) FontWeight.Bold else FontWeight.Normal
    val pastCardElevation = if(!reservedTreatment) CardDefaults.cardElevation(2.dp) else CardDefaults.cardElevation(0.dp)


    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column(
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
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "진료 일정",
                    fontSize = 38.sp,
                    color = Color(0xFF191A26),
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.add_button),
                    contentDescription = "진료 일정 추가",
                    modifier = Modifier
                        .size(42.dp)
                        .clickable {
                            /* TODO 진료 일정 추가 화면으로 이동 */
                        }
                )
            }
            Spacer(modifier = Modifier.height(34.dp))
            Card(
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(Color(0xFFF6F7FA)),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 3.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(40.dp),
                        colors = CardDefaults.cardColors(reservedCardColor),
                        elevation = reservedCardElevation,
                        onClick = { reservedTreatment = true},
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "예약된 진료",
                            fontSize = 14.sp,
                            fontWeight = reservedCardTextFontWeight,
                            color = reservedCardTextColor,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Card(
                        shape = RoundedCornerShape(40.dp),
                        colors = CardDefaults.cardColors(pastCardColor),
                        elevation = pastCardElevation,
                        onClick = { reservedTreatment = false},
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "지난 진료",
                            fontSize = 14.sp,
                            fontWeight = pastCardTextFontWeight,
                            color = pastCardTextColor,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))

            if (!reservedTreatment) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ) {
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "삼성서울병원",
                                    color = textColor,
                                    fontSize = 13.sp
                                )
                                Card(
                                    shape = MaterialTheme.shapes.small,
                                    colors = CardDefaults.cardColors(dateBackgroundColor)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(vertical = 6.dp, horizontal = 8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.date),
                                            contentDescription = "날짜",
                                            tint = dateColor,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "2023.01.03  오전 11:00",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.eye),
                                        contentDescription = "안과 아이콘",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "안과",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "이연욱 교수",
                                        color = textColor,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "삼성서울병원",
                                    color = textColor,
                                    fontSize = 13.sp
                                )
                                Card(
                                    shape = MaterialTheme.shapes.small,
                                    colors = CardDefaults.cardColors(dateBackgroundColor)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(vertical = 6.dp, horizontal = 8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.date),
                                            contentDescription = "날짜",
                                            tint = dateColor,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "2023.01.03  오전 11:00",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.eye),
                                        contentDescription = "안과 아이콘",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "안과",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "이연욱 교수",
                                        color = textColor,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (reservedTreatment) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ) {
                    item {
                        Card(
                            onClick = { /*TODO 진료 일정 수정 화면 연결*/ },
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "강남 세브란스 병원",
                                        color = textColor,
                                        fontSize = 13.sp
                                    )
                                    Box {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "더 보기",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    optionsExpanded = true
                                                }
                                        )
                                        DropdownMenu(
                                            expanded = optionsExpanded,
                                            onDismissRequest = { optionsExpanded = false },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                        ) {
                                            options.forEach {
                                                DropdownMenuItem(
                                                    onClick = {
                                                        selectedOption = it
                                                        optionsExpanded = false
                                                    }
                                                ) {
                                                    Text(text = it)
                                                }
                                            }
                                        }
                                    }
                                }
                                Card(
                                    shape = MaterialTheme.shapes.small,
                                    colors = CardDefaults.cardColors(dateBackgroundColor)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(vertical = 6.dp, horizontal = 8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.date),
                                            contentDescription = "날짜",
                                            tint = dateColor,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "2024.07.15  오후 2:45",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.eye),
                                        contentDescription = "안과 아이콘",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "안과",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "조종호 교수",
                                        color = textColor,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            onClick = { /*TODO 진료 일정 수정 화면 연결*/ },
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "강남 세브란스",
                                        color = textColor,
                                        fontSize = 13.sp
                                    )
                                    Box {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "더 보기",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    optionsExpanded1 = true
                                                }
                                        )
                                        DropdownMenu(
                                            expanded = optionsExpanded1,
                                            onDismissRequest = { optionsExpanded1 = false },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                        ) {
                                            options.forEach {
                                                DropdownMenuItem(
                                                    onClick = {
                                                        selectedOption1 = it
                                                        optionsExpanded1 = false
                                                    }
                                                ) {
                                                    Text(text = it)
                                                }
                                            }
                                        }
                                    }
                                }
                                Card(
                                    shape = MaterialTheme.shapes.small,
                                    colors = CardDefaults.cardColors(dateBackgroundColor)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(vertical = 6.dp, horizontal = 8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.date),
                                            contentDescription = "날짜",
                                            tint = dateColor,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "2024.07.16  오전 08:00",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.eye),
                                        contentDescription = "안과 아이콘",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "안과",
                                        color = Color.LightGray,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "문일준 교수",
                                        color = textColor,
                                        fontSize = 15.sp
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