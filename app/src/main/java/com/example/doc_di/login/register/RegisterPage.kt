package com.example.doc_di.login.register

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.register.RegisterImpl
import com.example.doc_di.login.GradientButton
import com.example.doc_di.login.register.registerinfo.RegisterEmail
import com.example.doc_di.login.register.registerinfo.RegisterName
import com.example.doc_di.login.register.registerinfo.RegisterPassword
import com.example.doc_di.ui.theme.LightBlue
import com.example.doc_di.ui.theme.MainBlue
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
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { RegisterTopBar(navController) },
        backgroundColor = Color.Transparent,
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
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
            ) {
                OutlinedTextField(
                    value = verifyCode.value,
                    onValueChange = { verifyCode.value = it },
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
                    label = {
                        Text(
                            text = "인증코드 입력",
                            color = Color.Black,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    placeholder = { Text(text = "이메일 코드 입력", color = Color.Black) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MainBlue,
                        unfocusedBorderColor = LightBlue,
                        cursorColor = MainBlue
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "인증코드 받기",
                    fontSize = 15.sp,
                    color = Color.Black,
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(top = 9.dp)
                        .width(100.dp)
                        .clickable{
                            scope.launch {
                                registerImpl.makeCode(email.value, context)
                            }
                        }
                )
            }
            RegisterPassword(password, passwordCheck, isPasswordAvailable)
            Spacer(modifier = Modifier.weight(2f))
            GradientButton(
                onClick = {
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