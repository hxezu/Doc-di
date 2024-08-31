package com.example.doc_di.etc

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.doc_di.R

@Composable
fun GoBack(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "뒤로가기",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}