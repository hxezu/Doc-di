package com.example.doc_di.searchresult

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PillInformationViewModel : ViewModel() {
    var showSearch = mutableStateListOf(true, false, false, false)
    var searchTitle = arrayOf("정보", "용법", "주의사항", "효능통계")
}