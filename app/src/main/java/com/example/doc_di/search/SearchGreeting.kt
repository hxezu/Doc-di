package com.example.doc_di.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.etc.observeAsState

@Composable
fun SearchGreeting(userViewModel: UserViewModel) {
    val userImage by userViewModel.userImage.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()

    if (userImage != null && userInfo != null){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Text(
                text = "좋은 하루에요,\n${userViewModel.userInfo.value?.name}님",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = Color.Black
            )

            Image(
                painter = BitmapPainter(userViewModel.userImage.value!!.asImageBitmap()),
                contentDescription = "검색 프로필 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(66.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}