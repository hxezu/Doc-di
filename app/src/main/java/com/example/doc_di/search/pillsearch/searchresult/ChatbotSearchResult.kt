package com.example.doc_di.search.pillsearch.searchresult

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.chatbot.ChatBotViewModel
import com.example.doc_di.domain.model.Medicine
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.clickableThrottleFirst
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.reminder.viewmodel.ReminderViewModel
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import com.example.doc_di.ui.theme.LightBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatbotSearchResult(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    searchViewModel: SearchViewModel,
    reviewViewModel: ReviewViewModel,
    reminderViewModel: ReminderViewModel,
    chatBotViewModel: ChatBotViewModel
) {
    val medicineList by chatBotViewModel.medicineList.observeAsState()
    LaunchedEffect(medicineList) {
        medicineList?.let {
            Log.d("ChatbotSearchResult", "Medicine List: $it")
        }
    }
    val titleColor = Color(0xFF303437)
    val pillList = medicineList?.map { it.toPill() } ?: emptyList()
    val isLoading = searchViewModel.isLoading.collectAsState().value

    LaunchedEffect(Unit) {
        userViewModel.userInfo.value?.email?.let { email ->
            reminderViewModel.getReminders(email)
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        searchViewModel.resetPillInfo()
        searchViewModel.resetPills()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(top = 68.dp, bottom = 106.dp)
        ) {
            GoBack(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickableThrottleFirst { navController.popBackStack() }
            )
            Text(
                text = "검색 결과",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                modifier = Modifier
                    .padding(vertical = 28.dp)
                    .align(Alignment.Start)
            )

            if (isLoading) {
                CircularProgressIndicator(color = LightBlue)
            }
            else {
                if (pillList.isEmpty()) {
                    Text(text = "조회되는 경구약제가 없습니다.", color = Color.Black)
                } else {
                    ShowPillList(pillList, navController, userViewModel, searchViewModel, reviewViewModel)
                }
            }
        }
    }
}

fun Medicine.toPill(): Pill {
    return Pill(
        bizrno = this.bizrno,
        changeDate = this.changeDate,
        chart = this.chart,
        className = this.className,
        classNo = this.classNo,
        colorClass1 = this.colorClass1,
        colorClass2 = this.colorClass2,
        drugShape = this.drugShape,
        ediCode = this.ediCode,
        entpName = this.entpName,
        entpSeq = this.entpSeq,
        etcOtcName = this.etcOtcName,
        formCodeName = this.formCodeName,
        imgRegistTs = this.imgRegistTs,
        itemEngName = this.itemEngName,
        itemImage = this.itemImage,
        itemName = this.itemName,
        itemPermitDate = this.itemPermitDate,
        itemSeq = this.itemSeq.toInt(), // 필요한 형 변환 수행
        lengLong = this.lengLong,
        lengShort = this.lengShort,
        lineBack = this.lineBack,
        lineFront = this.lineFront,
        markCodeBack = this.markCodeBack,
        markCodeBackAnal = this.markCodeBackAnal,
        markCodeBackImg = this.markCodeBackImg,
        markCodeFront = this.markCodeFront,
        markCodeFrontAnal = this.markCodeFrontAnal,
        markCodeFrontImg = this.markCodeFrontImg,
        printBack = this.printBack,
        printFront = this.printFront,
        thick = this.thick,
        rateTotal = this.rateTotal.toInt(), // 필요한 형 변환 수행
        rateAmount = this.rateAmount.toInt() // 필요한 형 변환 수행
    )
}
