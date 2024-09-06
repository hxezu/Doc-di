package com.example.doc_di.search.pillsearch.searchresult.pill_information

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ReviewViewModel: ViewModel() {
    val showSearch = mutableStateListOf(true, false, false, false)
}