package com.example.doc_di.searchresult

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.doc_di.home.BtmBarViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PillInformation(
    navController: NavController,
    pillViewModel: PillInformationViewModel,
    btmBarViewModel: BtmBarViewModel
) {
    val titleColor = Color(0xFF303437)
    val cardTitleColor = Color(0xFF333333)
    val cardDetailTextColor = Color(0xFF747F9E)
    val buttonColor = Color(0xFF007AEB)
    val starColor = Color(0xFFFFC000)
    val statisticNameColor = Color(0xFF090F47)
    val barGraphColor = Color(0xFF4157FF)
    val statisticTextColor = Color.Gray
    val barBackgroundColor = Color.LightGray
    val reviewPercentage = listOf(
        5 to 67,
        4 to 20,
        3 to 7,
        2 to 0,
        1 to 1
    )

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(top = 48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "타이레놀정 160mg",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .align(Alignment.Start)
            )
            Image(
                painter = painterResource(id = R.drawable.pill_image),
                contentDescription = "검색 결과 약 이미지",
                modifier = Modifier
                    .size(246.dp, 132.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical =  16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ){
                items(4){ index ->
                    Button(
                        onClick = {
                            for (j in 0 until pillViewModel.showSearch.size) {
                                pillViewModel.showSearch[j] = index == j
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            if(pillViewModel.showSearch[index] )buttonColor else Color.Transparent
                        ),
                        border = if(!pillViewModel.showSearch[index]) BorderStroke(
                            1.dp,
                            Color.LightGray
                        )
                        else null,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(48.dp)
                            .width(100.dp)
                    ) {
                        Text(
                            text = pillViewModel.searchTitle[index],
                            color = if(pillViewModel.showSearch[index]) Color.White else Color.LightGray,
                            fontSize = 13.sp,
                            fontWeight = if(pillViewModel.showSearch[index]) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            if(pillViewModel.showSearch[0]){
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ){
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "외형정보", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "성상 : 백색의 장방형 정제", color = cardDetailTextColor, fontSize = 10.sp)
                                Text(text = "제형 : 나정", color = cardDetailTextColor, fontSize = 10.sp)
                                Text(text = "모양: 장방형", color = cardDetailTextColor, fontSize = 10.sp)
                                Text(text = "색상 : 하양", color = cardDetailTextColor, fontSize = 10.sp)
                                Text(text = "식별표기 : (앞)TY-160, (뒤)분할선", color = cardDetailTextColor, fontSize = 10.sp)
                            }
                        }
                    }

                    item{
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "성분 정보", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "제피아세트아미노펜 177.78mg", color = cardDetailTextColor, fontSize = 10.sp)
                            }
                        }
                    }

                    item{
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "보관 방법", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "밀폐 용기 실온 보관", color = cardDetailTextColor, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            if(pillViewModel.showSearch[1]){
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ){
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "만 12세 이하의 소아", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "다음 1회 권장 용량을 4-6시간 마다 필요시 복용한다. 이 약은 가능한 최단기간동안 최소 유효 용량으로 복용하며, 1일 5회(75mg/kg)를 초과하여 복용하지 않는다.", color = cardDetailTextColor, fontSize = 10.sp,lineHeight = 14.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "몸무게를 아는 경우 몸무게에 따른 용량(10~15mg/kg)으로 복용하는 것이 더 적절하다. ", color = cardDetailTextColor, fontSize = 10.sp, lineHeight = 14.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Image(painter = painterResource(id = R.drawable.usage_example), contentDescription = "용법 이미지")
                            }
                        }
                    }
                }
            }

            if(pillViewModel.showSearch[2]){
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ){
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "경고", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "1) 매일  세 잔 이상 정기적으로 술을 마시는 사람이 이 약이나 다른 해열 진통제를 복용해야 할 경우 반드시 의사 또는 약사와 상의해야 한다. 이러한 사람이 이 약을 복용하면 간손상이 유발될 수 있다.", color = cardDetailTextColor, fontSize = 10.sp, lineHeight = 14.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "2) 아세트아미노펜을 복용한 환자에서 매우 드물게 급성 전신성 발진성 농포증(급성 전신성 발진성 고름물집증)(AGEP), 스티븐스  - 존슨 증후군(SJS), 독성 표피 괴사용해(TEN)와 같은 중대한 피부 반응이 보고되었고, 이러한 중대한 피부반응은 치명적일 수 있다. 따라서 이러한 중대한 피부반응의 징후에 대하여 환자들에게 충분히 알리고, 이 약 투여 후 피부발진이나 다른 과민반응의 징후가 나타나면 즉시 복용을 중단하도록 하여야 한다.", color = cardDetailTextColor, fontSize = 10.sp, lineHeight = 14.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "3) 이 약은 아세트아미노펜을 함유하고 있다. 아세트아미노펜으로 일일 최대 용량(4,000mg)을 초과할 경우 간손상을 일으킬 수 있으므로 이 약을 일일 최대 용량(4,000mg)을 초과하여 복용하여서는 아니되며, 아세트아미노펜을 포함하는 다른 제품과 함께 복용하여서는 안 된다.", color = cardDetailTextColor, fontSize = 10.sp, lineHeight = 14.sp)
                            }
                        }
                    }

                    item{
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(text = "다음과 같은 사람은 이 약을 복용하지 말 것", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "1) 이 약에 과민증 환자", color = cardDetailTextColor, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            if(pillViewModel.showSearch[3]){
                Row () {
                    Column {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "4.4", fontSize = 36.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Sharp.Star,
                                contentDescription = "전체 평점 별",
                                tint = starColor,
                                modifier = Modifier
                                    .size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "평점 923 개", fontSize = 15.sp, color = statisticTextColor)
                        Text(text = "리뷰 257 개", fontSize = 15.sp, color = statisticTextColor)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(start = 12.dp, top = 4.dp)
                            .width(1.dp)
                            .height(120.dp)
                            .background(Color.LightGray)

                    )
                    Column (
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        reviewPercentage.forEach { (stars, percentage) ->
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "$stars", color= statisticTextColor, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(1.dp))
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = "통계 별",
                                    tint = starColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LinearProgressIndicator(
                                    progress = percentage/100f,
                                    color = barGraphColor,
                                    trackColor = barBackgroundColor,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if(percentage / 10 ==0) "$percentage%  "
                                            else "$percentage%",
                                    fontSize = 14.sp,
                                    color = statisticTextColor
                                )
                            }
                        }
                    }
                }

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ){
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical =  16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Filled.Star, contentDescription = "리뷰 별", tint = starColor, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "4.2", color = statisticTextColor, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = "2020-10-05", color = statisticTextColor, fontSize =  14.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "이 * 우", color = statisticNameColor, fontSize = 14.sp, )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "개인적으로는 두통 등의 통증에 대해서 타이레놀이 잘 드는 것 같습니다. 그래서 두통이 느껴질 때 자연스럽게 찾게 되는 것 같습니다.", color = statisticTextColor, fontSize = 14.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Filled.Star, contentDescription = "리뷰 별", tint = starColor, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "4.2", color = statisticTextColor, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = "2021-03-23", color = statisticTextColor, fontSize =  14.sp)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "김 * 영", color = statisticNameColor, fontSize = 14.sp, )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "타이레놀 160mg 효과 생각보다 나은듯 해요.\n먹고난후 상태가 이정도이면 굿잡 !", color = statisticTextColor, fontSize = 14.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}