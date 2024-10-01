package com.example.doc_di.home.account_manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountImpl
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ModifyLogoutAccountDelete(navController: NavController, userViewModel: UserViewModel) {
    val accountImpl = AccountImpl(RetrofitInstance.accountApi)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showResignDialog by rememberSaveable { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 56.dp)
    ) {
        GoBack(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Start)
                .clickable { navController.popBackStack() }
        )
        Text(
            text = "회원정보 수정",
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .clickable { navController.navigate(Routes.profile.route) }
        )
        Divider(color = Color.LightGray)
        Text(
            text = "로그아웃",
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .clickable {
                    scope.launch {
                        accountImpl.logoutAccount(context, navController, userViewModel)
                    }
                }
        )
        Divider(color = Color.LightGray)
        Text(
            text = "계정탈퇴",
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .clickable {
                    showResignDialog = true
                }
        )
        Divider(color = Color.LightGray)
        if (showResignDialog) {
            ResignDialog(
                onDismissRequest = { showResignDialog = false },
                onConfirmation = {
                    scope.launch {
                        accountImpl.deleteAccount(context, navController, userViewModel)
                    }
                }
            )
        }
    }
}