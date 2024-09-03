package com.example.doc_di.login.register

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.register.RegisterImpl
import com.example.doc_di.login.GradientButton
import com.example.doc_di.login.register.registerinfo.RegisterEmail
import com.example.doc_di.login.register.registerinfo.RegisterName
import com.example.doc_di.login.register.registerinfo.RegisterPassword
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController) {
    val registerImpl = RegisterImpl(RetrofitInstance.registerApi)

    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordCheck = rememberSaveable { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val isAllWritten by remember {
        derivedStateOf {
            name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty()
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
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { RegisterTopBar(navController) },
        backgroundColor = Color.Transparent
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.weight(2f))
            ProfileImage(imageUri, imageBitmap, bitmap, context)
            Spacer(modifier = Modifier.weight(1f))
            RegisterName(name, isNameAvailable)
            RegisterEmail(email)
            RegisterPassword(password, passwordCheck, isPasswordAvailable)
            Spacer(modifier = Modifier.weight(2f))
            GradientButton(
                onClick = {
                    scope.launch {
                        registerImpl.register(
                            email.value,
                            password.value,
                            name.value,
                            context,
                            isAllWritten,
                            isAllAvailable,
                            navController,
                            bitmap
                        )
                    }
                },
                gradientColors = gradientColor,
                cornerRadius = cornerRadius,
                roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                buttonName = "계정 생성"
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}