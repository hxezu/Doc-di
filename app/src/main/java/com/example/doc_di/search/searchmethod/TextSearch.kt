package com.example.doc_di.search.searchmethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.etc.Routes
import com.example.doc_di.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSearch(navController: NavController, searchViewModel: SearchViewModel) {
    val howSearchButtonColor = Color(0xFF007AEB)
    val mainSearchColor = Color(0xFF1892FA)

    var nameSearch by remember{ mutableStateOf("")}
    val option = mutableMapOf<String, String>()

    fun doSearch(){
        option["name"] = nameSearch
        searchViewModel.setOptions(option)
        searchViewModel.searchPillsByOptions()
        navController.navigate(Routes.searchResult.route)
    }


    OutlinedTextField(
        value = nameSearch,
        onValueChange = {nameSearch = it},
        leadingIcon = {
            Row(
                modifier = Modifier.padding(start = 12.dp) // 왼쪽에 여백 추가
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "제품명 검색",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        placeholder = {
            Text(
                text = "제품명 입력",
                fontSize = 16.sp,
                color = Color.LightGray,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = howSearchButtonColor,
            unfocusedBorderColor = howSearchButtonColor
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onSearch = { doSearch() }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp, horizontal = 50.dp)
            .height(60.dp)
    )

    androidx.compose.material.Button(
        onClick = { doSearch() },
        colors = androidx.compose.material.ButtonDefaults.textButtonColors(mainSearchColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Text(
            text = "검색",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}