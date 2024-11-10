package com.example.doc_di.home.account_manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.clickableThrottleFirst

@Composable
fun AppDescription(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 68.dp)
    ) {
        GoBack(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Start)
                .clickableThrottleFirst {
                    navController.popBackStack()
                }
        )

        val guideItems = listOf(
            "홈 화면" to "진료일정 섹션: 리마인더에서 추가한 진료일정이 가장 가까운 순서로 표시되고, 이를 클릭하여 예정된 진료일정을 열람할 수 있습니다.\n\n" +
                    "복용알림 섹션: 리마인더에서 추가한 약 중 금일 복용해야 할 약이 표시됩니다. 이를 클릭하여 해당 약에 대한 검색 결과를 얻을 수 있습니다.",
            "알약 검색 화면" to "제품명 검색: 알약명을 입력하여 검색을 진행합니다.(알약명의 일부만 입력 가능)\n\n" +
                    "모양 검색: 알약 앞/뒤 면의 식별 표기를 입력하고(순서 무관), 색상과 모양을 선택하여 검색을 진행합니다.\n\n" +
                    "사진 검색: 단순한 배경에서 선명하게 촬영된 알약 사진으로 검색할수록 검색 결과의 정확도가 올라갑니다.\n\n" +
                    "효능통계: 사용자가 알약에 대한 리뷰를 작성하거나 다른 사용자들의 리뷰와 평점을 확인할 수 있는 탭입니다. " +
                    "리마인더에 등록했던 약이리면 리뷰를 작성할 수 있습니다.",
            "챗봇 화면" to "챗봇과의 대화 기록 리스트 : 똑디와 대화했던 기록들을 모두 확인할 수 있습니다.\n\n" +
                            "채팅창 : 똑디와 사용자가 대화를 할 수 있는 화면입니다. 똑디에게 찾고 싶은 알약을 물어보고 본인이 겪고 있는 증상에 대해 이야기를 나누어보아요.",
            "리마인더 화면" to "통합 리마인더 : 해당 날짜에 예정된 복용 약 및 진료 일정을 확인할 수 있는 화면입니다.\n\n" +
                    "복용 알림 리마인더 : 사용자가 복용해야 할 약에 대한 리마인더를 추가하고 알림을 받습니다.\n\n" +
                    "진료 일정 리마인더 : 예정된 진료 일정을 등록하여 잊지 않고 진료를 받을 수 있도록 합니다."
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            items(guideItems) { (title, description) ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = title,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            text = description,
                            color = Color.Black,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}