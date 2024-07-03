package com.example.doc_di.search

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class SearchViewModel :ViewModel(){
    var showSearch = mutableStateListOf(false,false,false)
    var searchTitle = arrayOf("제품명 검색", "모양 검색", "사진 검색")
}