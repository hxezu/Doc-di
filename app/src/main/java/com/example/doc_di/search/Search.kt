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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.doc_di.etc.Routes
import com.example.doc_di.search.pillsearch.PillSearch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Search(
    navController: NavController,
    searchViewModel: SearchViewModel,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 106.dp)
        ) {
            Spacer(modifier = Modifier.weight(2f))
            SearchGreeting(userViewModel)
            Spacer(modifier = Modifier.weight(1f))
            PillSearch(navController, searchViewModel)
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = "건강보험심사평가원 조회",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, bottom = 20.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 52.dp)
                ) {
                    Image(painter = painterResource(
                        id = R.drawable.medical_record
                    ),
                        contentDescription = "진료 기록 조회",
                        modifier = Modifier
                            .size(130.dp)
                            .clickable {
                                navController.navigate(Routes.medicalAppointmentRecord.route)
                            }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.prescription_record),
                        contentDescription = "처방 기록 조회",
                        modifier = Modifier
                            .size(130.dp)
                            .clickable {
                                navController.navigate(Routes.prescriptionRecord.route)
                            }
                    )
                }
            }
        }
    }
}