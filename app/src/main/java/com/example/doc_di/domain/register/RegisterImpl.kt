package com.example.doc_di.domain.register

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
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

class RegisterImpl(private val registerApi: RegisterApi) {
    suspend fun register(
        email: String,
        code: String,
        password: String,
        name: String,
        context: Context,
        isAllWritten: Boolean,
        isAllAvailable: Boolean,
        navController: NavController,
        bitmap: MutableState<Bitmap?>,
    ) {
        if (isAllWritten && isAllAvailable) {
            CoroutineScope(Dispatchers.IO).launch {
                val checkCodeDTO = CheckCodeDTO(
                    email = email,
                    code = code
                )
                val checkCodeResponse = RetrofitInstance.registerApi.checkCode(checkCodeDTO)
                if (checkCodeResponse.code() == 200){
                    val joinDTO = JoinDTO(
                        email = email,
                        password = password,
                        name = name,
                        image = email // 서버에서 이거 뒤에 .jpg 자동으로 붙여서 경로 저장
                    )
                    val file = File(context.cacheDir, "${email}.jpg")
                    FileOutputStream(file).use { out ->
                        bitmap.value!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }

                    val joinJson = Gson().toJson(joinDTO)
                    val joinRequestBody = joinJson.toRequestBody("application/json".toMediaTypeOrNull())
                    val joinPart = MultipartBody.Part.createFormData("joinDto", null, joinRequestBody)

                    val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                    val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                    val joinResponse = registerApi.join(joinPart, filePart)
                    if (joinResponse.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.login.route) { navController.popBackStack() }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "인증코드 불일치", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "모든 정보를 정확히 기입해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun makeCode(
        email: String,
        context:Context
    ){
        if(email.isNotEmpty()){
            val codeResponse = RetrofitInstance.registerApi.makeCode(email)
            if (codeResponse.code() == 201){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"이메일에 코드 발급",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"이메일이 유효하지 않음",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            withContext(Dispatchers.Main){
                Toast.makeText(context,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }
}