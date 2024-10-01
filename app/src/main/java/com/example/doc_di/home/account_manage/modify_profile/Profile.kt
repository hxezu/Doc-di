package com.example.doc_di.home.account_manage.modify_profile

import ModifyName
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountImpl
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.login.UserViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Profile(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val userImage by userViewModel.userImage.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()
    val accountImpl = AccountImpl(RetrofitInstance.accountApi)

    val isNameAvailable = rememberSaveable { mutableStateOf(true) }
    val isPasswordAvailable = rememberSaveable { mutableStateOf(false) }

    val name = rememberSaveable { mutableStateOf(userInfo!!.name) }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordCheck = rememberSaveable { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val bitmap = remember { mutableStateOf(userImage) }

    val isAllWritten by remember {
        derivedStateOf {
            name.value.isNotEmpty() && password.value.isNotEmpty()
        }
    }

    val isAllAvailable by remember {
        derivedStateOf {
            isNameAvailable.value && isPasswordAvailable.value
        }
    }

    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 68.dp)
    ) {
        GoBack(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Start)
                .clickable { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.weight(1.5f))
        ModifyProfileImage(imageUri, imageBitmap, bitmap, context)
        Spacer(modifier = Modifier.weight(0.5f))
        ModifyName(name, isNameAvailable)
        ModifyPassword(password, passwordCheck, isPasswordAvailable)
        Spacer(modifier = Modifier.weight(2f))
        Button(
            onClick = {
                scope.launch {
                    accountImpl.modifyProfile(
                        userInfo!!.email,
                        password.value,
                        name.value,
                        context,
                        isAllWritten,
                        isAllAvailable,
                        navController,
                        bitmap,
                        userViewModel
                    )
                }
            },
            colors = ButtonDefaults.textButtonColors(Color(0xFF007AEB)),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "프로필 수정",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}