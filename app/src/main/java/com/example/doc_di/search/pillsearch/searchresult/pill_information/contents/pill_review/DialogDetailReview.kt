package com.example.doc_di.search.pillsearch.searchresult.pill_information.contents.pill_review

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue

@Composable
fun DialogDetailReview(reviewText: MutableState<String>) {
    val focusManager = LocalFocusManager.current
    Text(
        text = "상세 리뷰",
        color = Color(0xFF9CA4AB),
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = reviewText.value,
        onValueChange = { reviewText.value = it },
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainBlue,
            unfocusedBorderColor = LightBlue,
            cursorColor = MainBlue
        ),
        maxLines = 4,
        modifier = Modifier
            .fillMaxWidth()
            .height(136.dp)
            .padding(horizontal = 20.dp)
    )
}