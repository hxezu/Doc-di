package com.example.doc_di.login.register

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doc_di.domain.model.JoinDTO
import com.example.doc_di.domain.pillsearch.RetrofitInstance
import com.example.doc_di.etc.Routes
import com.example.doc_di.login.register.registerinfo.RegisterBirthdate
import com.example.doc_di.login.register.registerinfo.RegisterBloodType
import com.example.doc_di.login.register.registerinfo.RegisterEmail
import com.example.doc_di.login.register.registerinfo.RegisterHeightWeight
import com.example.doc_di.login.register.registerinfo.RegisterName
import com.example.doc_di.login.register.registerinfo.RegisterPassword
import com.example.doc_di.login.register.registerinfo.RegisterPhone
import com.example.doc_di.login.register.registerinfo.RegisterSex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController) {

    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordCheck = rememberSaveable { mutableStateOf("") }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val birthDate = rememberSaveable { mutableStateOf("") }
    val sex = rememberSaveable { mutableStateOf("") }
    val height = rememberSaveable { mutableStateOf("") }
    val weight = rememberSaveable { mutableStateOf("") }
    val bloodType = rememberSaveable { mutableStateOf("") }

    val isAllWritten by remember{
        derivedStateOf {
            name.value.isNotEmpty() &&
            email.value.isNotEmpty()&&
            password.value.isNotEmpty()&&
            phoneNumber.value.isNotEmpty()&&
            birthDate.value.isNotEmpty()&&
            sex.value.isNotEmpty()&&
            height.value.isNotEmpty()&&
            weight.value.isNotEmpty()&&
            bloodType.value.isNotEmpty()
        }
    }

    val isNameAvailable = rememberSaveable { mutableStateOf(false) }
    val isPasswordAvailable = rememberSaveable { mutableStateOf(false) }
    val isPhoneNumberAvailable = rememberSaveable { mutableStateOf(false) }
    val isHeightAvailable = rememberSaveable { mutableStateOf(false) }
    val isWeightAvailable = rememberSaveable { mutableStateOf(false) }

    val isAllAvailable by remember {
        derivedStateOf {
            isNameAvailable.value &&
            isPasswordAvailable.value &&
            isPhoneNumberAvailable.value &&
            isHeightAvailable.value &&
            isWeightAvailable.value
        }
    }

    val gradientColor = listOf(Color(0xFF0052D4), Color(0xFF4364F7), Color(0xFF6FB1FC))
    val cornerRadius = 16.dp

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { RegisterTopBar(navController) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            RegisterName(name, isNameAvailable)
            RegisterEmail(email)
            RegisterPassword(password, passwordCheck, isPasswordAvailable)
            RegisterPhone(phoneNumber, isPhoneNumberAvailable)
            RegisterBirthdate(birthDate)
            RegisterSex(sex)
            RegisterHeightWeight(height, weight, isHeightAvailable, isWeightAvailable)
            RegisterBloodType(bloodType)
            Spacer(modifier = Modifier.weight(1f))
            GradientButton(
                onClick = {
                    if (isAllWritten){
                        if(isAllAvailable){
                            CoroutineScope(Dispatchers.IO).launch {
                                val joinDTO = JoinDTO(
                                    email = email.value,
                                    password = password.value,
                                    name = name.value,
                                    sex = sex.value,
                                    birthday = birthDate.value,
                                    height = height.value.toShort(),
                                    weight = weight.value.toShort(),
                                    bloodType = bloodType.value,
                                    phoneNum = phoneNumber.value
                                )

                                val response = RetrofitInstance.api.join(joinDTO)
                                if (response.isSuccessful){
                                    withContext(Dispatchers.Main){
                                        Toast.makeText(context, "회워가입 성공", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Routes.login.route) { navController.popBackStack() }
                                    }
                                }
                                else {
                                    withContext(Dispatchers.Main){
                                        Toast.makeText(context, "가입 실패", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(context, "형식에 맞게 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        if (isAllAvailable){
                            Toast.makeText(context, "입력란을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, "회원 정보를 다시 기입해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                gradientColors = gradientColor,
                cornerRadius = cornerRadius,
                roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                buttonName = "계생 생성"
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}