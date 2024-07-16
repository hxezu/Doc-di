package com.example.doc_di.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Search(navController: NavController, searchViewModel: SearchViewModel) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        )  {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            ) {
                Text(
                    text = "좋은 하루에요,\n김유정님",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Image(
                    painter = painterResource(id = R.drawable.user_image),
                    contentDescription = "검색 프로필",
                    modifier = Modifier
                        .size(64.dp)
                )
            }


            /*{ TODO("검색화면 알약 검색 유동적 card") } */
            Text(
                text = "알약 검색",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            )
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            ) {
                Button(onClick = {
                    searchViewModel.showSearch[0] = true
                    searchViewModel.showSearch[1] = false
                    searchViewModel.showSearch[2] = false
                    navController.navigate(Routes.searchMethod.route)
                }) {
                    Text(text = "제품명 검색")
                }
                Button(onClick = {
                    searchViewModel.showSearch[0] = false
                    searchViewModel.showSearch[1] = true
                    searchViewModel.showSearch[2] = false
                    navController.navigate(Routes.searchMethod.route)
                }) {
                    Text(text = "모양 검색")
                }
                Button(onClick = {
                    searchViewModel.showSearch[0] = false
                    searchViewModel.showSearch[1] = false
                    searchViewModel.showSearch[2] = true
                    navController.navigate(Routes.searchMethod.route)
                }) {
                    Text(text = "사진 검색")
                }
            }

            Text(
                text = "건강보험심사평가원 조회",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            ) {
                Image(painter = painterResource(
                    id = R.drawable.medical_record),
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

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    val navController = rememberNavController()
    val searchViewModel:SearchViewModel = viewModel()
    Search(navController = navController, searchViewModel = searchViewModel)
}