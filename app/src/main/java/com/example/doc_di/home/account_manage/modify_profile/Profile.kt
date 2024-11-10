package com.example.doc_di.home.account_manage.modify_profile

import ModifyName
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountImpl
import com.example.doc_di.etc.GoBack
import com.example.doc_di.etc.clickableThrottleFirst
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.etc.throttleFirst
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.login.rememberImeState
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
    val scrollState = rememberScrollState()
    val imeState = rememberImeState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = imeState) {
        if (imeState.value) {
            scrollState.animateScrollTo(0)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 68.dp)
            .verticalScroll(scrollState)
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            )
    ) {
        GoBack(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Start)
                .clickableThrottleFirst {
                    keyboardController?.hide()
                    navController.popBackStack()
                }
        )
        Spacer(modifier = Modifier.weight(1.5f))
        ModifyProfileImage(imageUri, imageBitmap, bitmap, context)
        Spacer(modifier = Modifier.weight(0.5f))
        ModifyName(name, isNameAvailable)
        ModifyPassword(password, passwordCheck, isPasswordAvailable)
        Spacer(modifier = Modifier.weight(2f))
        Button(
            onClick = {
                {
                    if (isNetworkAvailable(context)) {
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
                        val nothing = ""
                    } else {
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                }.throttleFirst()
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