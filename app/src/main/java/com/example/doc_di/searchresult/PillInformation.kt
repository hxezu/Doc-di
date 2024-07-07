package com.example.doc_di.searchresult

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PillInformation(navController: NavController) {
    val titleColor = Color(0xFF303437)
    val cardTitleColor = Color(0xFF333333)
    val cardDetailTextColor = Color(0xFF747F9E)
    val buttonColor = Color(0xFF007AEB)
    val unSelectedButtonColor = Color.LightGray

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
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
                        .clickable { navController.navigate(Routes.search.route) }
                )
            }
            Text(
                text = "타이레놀정 160mg",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .padding(start = 16.dp)
                    .align(Alignment.Start)
            )
            // 이미지

            // 가로로 정보 용병 주의사항 효능통계 돌릴 수 있게

            // 각각에 해당하는 내용


//            Card(
//                shape = MaterialTheme.shapes.medium,
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//                    Text(
//                        text = "타이레놀8시간이알서방정325mg",
//                        color = cardPillTextColor,
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier
//                            .padding(start = 28.dp)
//                    )
//                    Spacer(modifier = Modifier.weight(1f))
//                    Icon(
//                        imageVector = Icons.Rounded.Star,
//                        tint = starColor,
//                        contentDescription = "별점",
//                    )
//                    Text(
//                        text = "4.5(834)",
//                        color = reviewTextColor,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier
//                            .padding(start = 4.dp, end = 16.dp)
//                    )
//                }
//            }
        }
    }
}