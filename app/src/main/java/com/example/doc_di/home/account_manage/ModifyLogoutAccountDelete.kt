package com.example.doc_di.home.account_manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.etc.Routes

@Composable
fun ModifyLogoutAccountDelete(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "뒤로가기",
            modifier = Modifier.size(32.dp).clickable { navController.popBackStack() }
        )
        Text(
            text = "회원정보 수정",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .clickable {
                    navController.navigate(
                        Routes.profile.route
                    )
                }
        )
        Divider(color = Color.LightGray)
        Text(
            text = "로그아웃",
            modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable {
                navController.navigate(Routes.login.route)
                /* TODO 서버와 연동하여 계정 로그아웃, 유저의 액세스 리프레쉬 없애고 백에는 이메일, 리프레쉬 토큰 전달하여 서버의 리프레쉬 토큰 삭제 */
            }
        )
        Divider(color = Color.LightGray)
        Text(
            text = "계정탈퇴",
            modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable {
                /* TODO 서버와 연동하여 계정 삭제 email만 주면 됨 */
            }
        )
        Divider(color = Color.LightGray)
    }
}