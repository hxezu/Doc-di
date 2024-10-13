package com.example.doc_di.search

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.domain.review.SearchHistory
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.UserViewModel

@Composable
fun PastSearchList(
    pastHistory: List<SearchHistory>?,
    context: Context,
    navController: NavController,
    userViewModel: UserViewModel,
    searchViewModel: SearchViewModel
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!pastHistory.isNullOrEmpty()){
            items(pastHistory){history ->
                Button(
                    onClick = {
                        searchViewModel.setSelectedPillByPillName(history.medicineName)
                        navController.navigate(Routes.searchResult.route)
                    },
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(22.dp),
                    elevation = null,
                    modifier = Modifier.height(48.dp)
                ) {
                    Row (horizontalArrangement = Arrangement.Center){
                        Text(text = " ${history.medicineName}", color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "기록 삭제",
                            tint = Color.Gray,
                            modifier = Modifier
                                .clickable {
                                    userViewModel.deletePillHistory(history.id, context, navController)
                                }
                        )
                    }
                }
            }
        }
    }
}