package com.example.doc_di.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.doc_di.R
import com.example.doc_di.etc.BtmBarItem
import com.example.doc_di.etc.Routes

class BtmBarViewModel : ViewModel(){
    var btmNavBarItems = mutableStateListOf<BtmBarItem>(
        BtmBarItem(
            title = "홈 화면",
            iconResourceId = R.drawable.home,
            route = Routes.home.route,
            selected = true,
        ),
        BtmBarItem(
            title = "검색 화면",
            iconResourceId = R.drawable.pill,
            route = Routes.search.route,
            selected = false,
        ),
        BtmBarItem(
            title = "챗봇 화면",
            iconResourceId = R.drawable.chatbot,
            route = Routes.chatListScreen.route,
            selected = false,
        ),
        BtmBarItem(
            title = "복용 관리 화면",
            iconResourceId = R.drawable.management,
            route = Routes.managementScreen.route,
            selected = false,
        )
    )
}