package com.example.doc_di.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.UserViewModel
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.LoadingHomeScreen
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import kotlin.system.exitProcess


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    reminderViewModel: ReminderViewModel
) {
    val titleColor = Color(0xFF404446)
    val cardPillColor = Color(0xFF202325)
    val alarmColor = Color(0xFF979C9E)
    val pinColor = Color(0xFF979C9E)
    val starColor = Color(0xFFFFC462)
    val cardTextColor = Color(0xFF72777A)

    val upcomingAppointment by reminderViewModel.upcomingAppointments.observeAsState()

    fun updateBtmBarItem(route: String) {
        btmBarViewModel.btmNavBarItems.forEach {
            it.selected = route == it.route
        }
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.route) {
                Routes.home.route -> { updateBtmBarItem(Routes.home.route) }
                Routes.search.route -> { updateBtmBarItem(Routes.search.route) }
                Routes.chatListScreen.route -> { updateBtmBarItem(Routes.chatListScreen.route) }
                Routes.managementScreen.route -> { updateBtmBarItem(Routes.managementScreen.route) }
            }
        }
    }

    BackHandler {
        if (navController.previousBackStackEntry?.destination?.route == null) {
            exitProcess(0)
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent,
    ) {
        if (userViewModel.userInfo.value == null) {
            LoadingHomeScreen()
        } else {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, bottom = 106.dp, top = 40.dp)
            ) {
                HomeGreeting(navController, userViewModel)
                UpcomingAppointment(upcomingAppointment, navController)

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
                                    /* TODO 혜주가 나중에 복용알림 리스트 뷰모델에 받으면 연결*/
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
}
