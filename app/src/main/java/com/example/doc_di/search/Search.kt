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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.example.doc_di.etc.SnappingLazyRow
import com.example.doc_di.etc.BtmBarViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Search(navController: NavController, searchViewModel: SearchViewModel, btmBarViewModel: BtmBarViewModel) {
    val items = mutableListOf(
        R.drawable.search_text,
        R.drawable.search_shape,
        R.drawable.search_image
    )

    val listState = rememberLazyListState()
    val scope= rememberCoroutineScope()

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) {
        Column (
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 106.dp)
        )  {
            Spacer(modifier = Modifier.weight(5f))
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
            Spacer(modifier = Modifier.weight(5f))
            Column {
                Text(
                    text = "알약 검색",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, bottom = 20.dp)
                )
                SnappingLazyRow(
                    items = items,
                    itemWidth = 280.dp,
                    onSelect = {
                        for (i in 0 until 3){
                            searchViewModel.showSearch[i] = i == it
                        }
                    },
                    scaleCalculator = { offset, halfRowWidth ->
                        (1f - minOf(1f, abs(offset).toFloat() / halfRowWidth) * 0.2f)
                    },
                    listState = listState,
                    modifier = Modifier
                        .height(310.dp)
                ) { index, item, scale ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(280.dp, 310.dp)
                            .clickable {
                                scope.launch {
                                    listState.animateScrollToItem(index)
                                    navController.navigate(Routes.searchMethod.route)
                                }
                            }
                            .scale(scale)
                    ) {
                        Image(
                            painter = painterResource(id = item),
                            contentDescription = "알약 검색 이미지"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(2f))

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

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    val navController = rememberNavController()
    val searchViewModel:SearchViewModel = viewModel()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    Search(navController = navController, searchViewModel = searchViewModel, btmBarViewModel = btmBarViewModel)
}