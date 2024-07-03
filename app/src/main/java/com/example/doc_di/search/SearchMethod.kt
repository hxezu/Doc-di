@file:Suppress("UnusedImport")

package com.example.doc_di.search

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
fun SearchMethod(
    navController:NavController,
    searchViewModel: SearchViewModel
) {
    var nameSearch by remember{ mutableStateOf("")}

    val howSearchButtonColor = Color(0xFF007AEB)
    val mainSearchColor = Color(0xFF1892FA)

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.backarrow),
                    contentDescription = "이전",
                    modifier = Modifier.size(44.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "검색 닫기",
                    modifier = Modifier
                        .size(44.dp)
                        .clickable{navController.navigate(Routes.search.route)}
                )
            }

            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
            ) {
                for (i in 0 until searchViewModel.showSearch.size) {
                    Button(
                        onClick = {
                            for (j in 0 until searchViewModel.showSearch.size) {
                                searchViewModel.showSearch[j] = i == j
                            }
                        },
                        border = if (!searchViewModel.showSearch[i]) BorderStroke(
                            1.dp,
                            Color.LightGray
                        )
                        else null,
                        colors = if (searchViewModel.showSearch[i]) ButtonDefaults.textButtonColors(
                            howSearchButtonColor
                        )
                        else ButtonDefaults.textButtonColors(Color.Transparent),
                        modifier = Modifier.size(width = 124.dp, height = 48.dp)
                    ) {
                        Text(
                            text = searchViewModel.searchTitle[i],
                            color = if (searchViewModel.showSearch[i]) Color.White else Color.LightGray,
                            fontSize = 14.sp,
                            fontWeight = if (searchViewModel.showSearch[i]) FontWeight.Bold else null
                        )
                    }
                }
            }

            if(searchViewModel.showSearch[0]){
                OutlinedTextField(
                    value = nameSearch,
                    onValueChange = {nameSearch = it},
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 12.dp) // 왼쪽에 여백 추가
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "제품명 검색",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = "제품명 입력",
                            fontSize = 16.sp,
                            color = Color.LightGray,
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = howSearchButtonColor,
                        unfocusedBorderColor = howSearchButtonColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(vertical = 70.dp)
                        .size(336.dp, 57.dp)
                )

                androidx.compose.material.Button(
                    onClick = { TODO("검색 결과 화면으로 이동" ) },
                    colors = androidx.compose.material.ButtonDefaults.textButtonColors(mainSearchColor),
                    modifier = Modifier
                        .size(328.dp, 60.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "검색",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
            if(searchViewModel.showSearch[1]){

            }
            if(searchViewModel.showSearch[2]){

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMethodPreview() {
    val navController = rememberNavController()
    val searchViewModel:SearchViewModel = viewModel()
    searchViewModel.showSearch[0] = true
    SearchMethod(navController = navController, searchViewModel = searchViewModel)
}