package com.example.doc_di.search.pillsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.SnappingLazyRow
import com.example.doc_di.search.SearchViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PillSearch(navController: NavController, searchViewModel: SearchViewModel) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val items = mutableListOf(
        R.drawable.search_text,
        R.drawable.search_shape,
        R.drawable.search_image
    )

    Column {
        Text(
            text = "알약 검색",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, bottom = 20.dp)
        )
        SnappingLazyRow(
            items = items,
            itemWidth = 280.dp,
            onSelect = {
                for (i in 0 until 3) {
                    searchViewModel.showSearch[i] = i == it
                }
            },
            scaleCalculator = { offset, halfRowWidth ->
                (1f - minOf(1f, abs(offset).toFloat() / halfRowWidth) * 0.2f)
            },
            listState = listState,
            modifier = Modifier
                .height(310.dp)
        ) { index, item, scale ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(280.dp, 340.dp)
                    .clickable {
                        scope.launch {
                            listState.animateScrollToItem(index)
                            navController.navigate(Routes.searchMethod.route)
                        }
                    }
                    .scale(scale)
            ) {
                Image(
                    painter = painterResource(id = item),
                    contentDescription = "알약 검색 이미지"
                )
            }
        }
    }
}