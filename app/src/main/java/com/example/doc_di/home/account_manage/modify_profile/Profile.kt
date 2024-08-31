package com.example.doc_di.home.account_manage.modify_profile

import ModifyName
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountDTO
import com.example.doc_di.etc.Routes
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Profile(navController: NavController) {
    val context = LocalContext.current

    val isNameAvailable = rememberSaveable { mutableStateOf(false) }
    val isPasswordAvailable = rememberSaveable { mutableStateOf(false) }

    val email by remember { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordCheck = rememberSaveable { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .padding(vertical = 68.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "뒤로가기",
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
                if (isAllWritten && isAllAvailable) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val accountDTO = AccountDTO(
                            email = "moder", /*TODO USer 뷰모델 연결하고 email 가져와야함*/
                            password = password.value,
                            name = name.value,
                            image = "moder" // 서버에서 이거 뒤에 .jpg 자동으로 붙여서 경로 저장
                        )
                        val file = File(context.cacheDir, "moder.jpg")
                        FileOutputStream(file).use { out ->
                            bitmap.value!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        }

                        val accountJson = Gson().toJson(accountDTO)
                        val accountRequestBody = accountJson.toRequestBody("application/json".toMediaTypeOrNull())
                        val accountPart = MultipartBody.Part.createFormData("userDto", null, accountRequestBody)

                        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                        val modifyResponse = RetrofitInstance.accountApi.modifyProfile(accountPart, filePart)
                        if (modifyResponse.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "프로필 수정 성공", Toast.LENGTH_SHORT).show()
                                navController.navigate( Routes.home.route) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "수정 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "모든 정보를 정확히 기입해 주세요.", Toast.LENGTH_SHORT).show()
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

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController = navController)
}