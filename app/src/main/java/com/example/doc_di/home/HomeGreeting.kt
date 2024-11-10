package com.example.doc_di.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.clickableThrottleFirst
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.login.UserViewModel


@Composable
fun HomeGreeting(navController: NavController, userViewModel: UserViewModel) {
    val greetTextColor = Color(0xFF303437)
    val userImage by userViewModel.userImage.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()

    if (userImage != null && userInfo != null){
        Image(
            painter = BitmapPainter(userViewModel.userImage.value!!.asImageBitmap()),
            contentDescription = "홈 프로필 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(66.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickableThrottleFirst {
                    navController.navigate(Routes.modifyLogoutAccountDelete.route)
                }
        )

        Text(
            text = "안녕하세요,\n${userViewModel.userInfo.value!!.name}님 \uD83D\uDE00",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
            color = greetTextColor
        )
    }
}