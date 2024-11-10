package com.example.doc_di.search.pillsearch.searchmethod

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.clickableThrottleFirst
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchmethod.imagesearch.ImageSearch
import com.example.doc_di.search.pillsearch.searchmethod.shapesearch.ShapeSearch
import com.example.doc_di.search.pillsearch.searchmethod.textsearch.TextSearch

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchMethod(
    navController: NavController,
    searchViewModel: SearchViewModel,
    btmBarViewModel: BtmBarViewModel,
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, btmBarViewModel) },
        containerColor = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 68.dp, bottom = 106.dp)
        ) {
            GoBack(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .size(30.dp)
                    .align(Alignment.Start)
                    .clickableThrottleFirst { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.height(40.dp))
            SearchMethodBar(searchViewModel)
            when {
                searchViewModel.showSearch[0] -> TextSearch(navController, searchViewModel)
                searchViewModel.showSearch[1] -> ShapeSearch(navController, searchViewModel)
                searchViewModel.showSearch[2] -> ImageSearch(navController, searchViewModel)
            }
        }
    }
}