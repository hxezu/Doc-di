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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.search.SearchViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PillInformation(
    navController: NavController,
    pillViewModel: PillInformationViewModel,
    btmBarViewModel: BtmBarViewModel,
    searchViewModel: SearchViewModel,
) {
    val selectedPill = searchViewModel.getSelectedPill()
    val selectedPillInfo = searchViewModel.pillInfo.collectAsState().value
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(selectedPill.itemImage)
            .size(Size.ORIGINAL)
            .build()
    ).state

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

    Scaffold(bottomBar = {
        BottomNavigationBar(
            navController = navController,
            btmBarViewModel = btmBarViewModel
        )
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp)
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
                text = selectedPill.itemName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )
            if (imageState is AsyncImagePainter.State.Error) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(246.dp, 132.dp)
                ) {
                    CircularProgressIndicator()
                }
            }
            if (imageState is AsyncImagePainter.State.Success) {
                Image(
                    painter = imageState.painter,
                    contentDescription = "검색 결과 약 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(246.dp, 132.dp)
                )
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(4) { index ->
                    Button(
                        onClick = {
                            for (j in 0 until pillViewModel.showSearch.size) {
                                pillViewModel.showSearch[j] = index == j
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            if (pillViewModel.showSearch[index]) buttonColor else Color.Transparent
                        ),
                        border = if (!pillViewModel.showSearch[index]) BorderStroke(
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
                            color = if (pillViewModel.showSearch[index]) Color.White else Color.LightGray,
                            fontSize = 13.sp,
                            fontWeight = if (pillViewModel.showSearch[index]) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            if (pillViewModel.showSearch[0]) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ) {
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
                                Text(
                                    text = "성상 : ${if (selectedPill.chart != "") selectedPill.chart else "-"}",
                                    color = cardDetailTextColor,
                                    lineHeight = 14.sp,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "제형 : ${
                                        when (selectedPill.formCodeName) {
                                            "" -> "-"
                                            "나정" -> "정제"
                                            else -> selectedPill.formCodeName
                                        }
                                    }",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "모양 : ${if (selectedPill.drugShape != "") selectedPill.drugShape else "-"}",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "색상 : ${if (selectedPill.colorClass1 != "") selectedPill.colorClass1 else "-"}, " +
                                            if (selectedPill.colorClass2 != "") selectedPill.colorClass2 else "-",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "분할선 : (앞) ${if (selectedPill.lineFront != "") "있음" else "없음"}, " +
                                            "(뒤) ${if (selectedPill.lineBack != "") "있음" else "없음"}",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "식별표기 : (앞) ${if (selectedPill.printFront != "") selectedPill.printFront else "-"}, " +
                                            "(뒤) ${if (selectedPill.printBack !="") selectedPill.printBack else "-"}",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

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
                                Text(text = "효능", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (selectedPillInfo.efcyQesitm != "") selectedPillInfo.efcyQesitm else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    lineHeight = 14.sp,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

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
                                Text(text = "보관 방법", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (selectedPillInfo.depositMethodQesitm != "") selectedPillInfo.depositMethodQesitm else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    lineHeight = 14.sp,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            if (pillViewModel.showSearch[1]) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ) {
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
                                Text(
                                    text = "복용법",
                                    color = cardTitleColor,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (selectedPillInfo.useMethodQesitm != "") selectedPillInfo.useMethodQesitm else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            if (pillViewModel.showSearch[2]) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ) {
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
                                Text(
                                    text = if (selectedPillInfo.atpnWarnQesitm != "") selectedPillInfo.atpnWarnQesitm else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

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
                                Text(text = "주의", color = cardTitleColor, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if(selectedPillInfo.atpnQesitm != "") selectedPillInfo.atpnQesitm else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

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
                                Text(
                                    text = "부작용",
                                    color = cardTitleColor,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text =if (selectedPillInfo.seQesitm != "") selectedPillInfo.seQesitm else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    lineHeight = 14.sp,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

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
                                Text(
                                    text = "약물 병용 주의",
                                    color = cardTitleColor,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if(selectedPillInfo.intrcQesitm != "") selectedPillInfo.intrcQesitm  else "- 정보 미제공 -",
                                    color = cardDetailTextColor,
                                    lineHeight = 14.sp,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            if (pillViewModel.showSearch[3]) {
                /* TODO 사용자가 복용중인 약 리스트안에 search result의 약이 있다면 효능통계 갔을 시 + Floating Button */
                /* TODO 이 버튼을 통해 후기 작성 화면? 으로 넘어가서 별점, 후기, 내용, 전송 구현 */

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 106.dp)
                ) {
                    item {
                        Row() {
                            Column {
                                Row(
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
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                reviewPercentage.forEach { (stars, percentage) ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(text = "$stars", color = statisticTextColor, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(1.dp))
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            contentDescription = "통계 별",
                                            tint = starColor
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        LinearProgressIndicator(
                                            progress = percentage / 100f,
                                            color = barGraphColor,
                                            trackColor = barBackgroundColor,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (percentage / 10 == 0) "$percentage%  "
                                            else "$percentage%",
                                            fontSize = 14.sp,
                                            color = statisticTextColor
                                        )
                                    }
                                }
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "리뷰 별",
                                        tint = starColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "4.2", color = statisticTextColor, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "2020-10-05",
                                        color = statisticTextColor,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "이 * 우", color = statisticNameColor, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "개인적으로는 두통 등의 통증에 대해서 타이레놀이 잘 드는 것 같습니다. 그래서 두통이 느껴질 때 자연스럽게 찾게 되는 것 같습니다.",
                                    color = statisticTextColor,
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp
                                )
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "리뷰 별",
                                        tint = starColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "4.2", color = statisticTextColor, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "2021-03-23",
                                        color = statisticTextColor,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "김 * 영", color = statisticNameColor, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "타이레놀 160mg 효과 생각보다 나은듯 해요.\n먹고난후 상태가 이정도이면 굿잡 !",
                                    color = statisticTextColor,
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}