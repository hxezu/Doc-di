package com.example.doc_di.login.register

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.register.RegisterImpl
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.login.GradientButton
import com.example.doc_di.login.register.registerinfo.RegisterEmail
import com.example.doc_di.login.register.registerinfo.RegisterName
import com.example.doc_di.login.register.registerinfo.RegisterPassword
import com.example.doc_di.login.register.registerinfo.VerificationCode
import com.example.doc_di.login.rememberImeState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController) {
    val registerImpl = RegisterImpl(RetrofitInstance.registerApi)

    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val verifyCode = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordCheck = rememberSaveable { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val isAllWritten by remember {
        derivedStateOf {
            name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty()
                    && passwordCheck.value.isNotEmpty() && verifyCode.value.isNotEmpty()
        }
    }

    val isNameAvailable = rememberSaveable { mutableStateOf(false) }
    val isPasswordAvailable = rememberSaveable { mutableStateOf(false) }

    val isAllAvailable by remember {
        derivedStateOf {
            isNameAvailable.value && isPasswordAvailable.value
        }
    }

    val gradientColor = listOf(Color(0xFF0052D4), Color(0xFF4364F7), Color(0xFF6FB1FC))
    val cornerRadius = 16.dp

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val imeState = rememberImeState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = imeState.value){
        if (imeState.value){
            scrollState.animateScrollTo(0)
        }
    }

    Scaffold(
        topBar = { RegisterTopBar(navController) },
        backgroundColor = Color.Transparent,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { focusManager.clearFocus() }
                )
        ) {
            Spacer(modifier = Modifier.weight(2f))
            ProfileImage(imageUri, imageBitmap, bitmap, context)
            Spacer(modifier = Modifier.weight(1f))
            RegisterName(name, isNameAvailable)
            RegisterEmail(email)
            VerificationCode(email, verifyCode, context)
            RegisterPassword(password, passwordCheck, isPasswordAvailable)
            Spacer(modifier = Modifier.weight(2f))
            GradientButton(
                onClick = {
                    if (isNetworkAvailable(context)) {
                        scope.launch {
                            registerImpl.register(
                                email.value,
                                verifyCode.value,
                                password.value,
                                name.value,
                                context,
                                isAllWritten,
                                isAllAvailable,
                                navController,
                                bitmap
                            )
                        }
                    }
                    else {
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                },
                gradientColors = gradientColor,
                cornerRadius = cornerRadius,
                roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                buttonName = "계정 생성",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}