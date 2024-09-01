package com.example.doc_di

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountDTO
import com.example.doc_di.login.loginpage.getEncryptedSharedPreferences
import com.example.doc_di.login.loginpage.saveAccessToken
import com.example.doc_di.login.loginpage.saveRefreshToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.launch
import java.security.SignatureException

class UserViewModel : ViewModel() {
    val secretKey =
        "af86bed1b346575a174c4026addb078151bd2484be48655c69bb8155f7d3c1f3cafd0bf5f2dab7ad665ce4f9e42df366d421a9dc041d78e377fab127d237b988"

    private val _userInfo = MutableLiveData<AccountDTO?>()
    val userInfo: LiveData<AccountDTO?> get() = _userInfo

    private val _userImage = MutableLiveData<Bitmap?>()
    val userImage: LiveData<Bitmap?> get() = _userImage

    fun fetchUser(context: Context, reLogin: () -> Unit) {
        viewModelScope.launch {
            try {
                val sharedPreferences = getEncryptedSharedPreferences(context)
                val accessToken = sharedPreferences.getString("access_token", null)

                if (!accessToken.isNullOrEmpty()) {
                    val email = getEmailFromToken(accessToken, secretKey)
                    if (email == null) {
                        Log.d("UserViewModelInfo", "access:$accessToken, email:$email")
                        return@launch
                    }

                    val userInfoResponse = RetrofitInstance.accountApi.getUserInfo(email, accessToken)
                    if (userInfoResponse.isSuccessful) {
                        _userInfo.postValue(userInfoResponse.body()!!.data)
                    }
                    else if (userInfoResponse.code() == 401) { // 만료된 경우
                        var refreshToken = sharedPreferences.getString("refresh_token", null)
                        if (!refreshToken.isNullOrEmpty()) {
                            val refreshResponse = RetrofitInstance.accountApi.reissueToken()
                            if (refreshResponse.isSuccessful) {
                                val newAccessToken = refreshResponse.headers()["access"]
                                newAccessToken?.let {
                                    saveAccessToken(context, it)
                                }

                                val retryResponse = RetrofitInstance.accountApi.getUserInfo(email, newAccessToken!!)
                                if (retryResponse.isSuccessful) {
                                    _userInfo.postValue(retryResponse.body()!!.data)
                                }

                                val cookie = refreshResponse.headers()["Set-Cookie"]
                                cookie?.split(";")?.forEach { cookie ->
                                    val cookiePair = cookie.split("=")
                                    if (cookiePair[0] == "refresh") {
                                        refreshToken = cookiePair[1].trim()
                                    }
                                }
                                saveRefreshToken(context, refreshToken!!)
                            } else {
                                reLogin()
                            }
                        }
                    }
                    else{
                        reLogin()
                    }

                    val imageResponse = RetrofitInstance.accountApi.getUserImage("profile/${email}.jpg", accessToken)
                    if (imageResponse.isSuccessful) {
                        val bitmap = BitmapFactory.decodeStream(imageResponse.body()?.byteStream())
                        _userImage.postValue(bitmap)
                    } else if (imageResponse.code() == 401) {
                        var refreshToken = sharedPreferences.getString("refresh_token", null)
                        if (!refreshToken.isNullOrEmpty()) {
                            val refreshResponse = RetrofitInstance.accountApi.reissueToken()
                            if (refreshResponse.isSuccessful) {
                                val newAccessToken = refreshResponse.headers()["access"]
                                newAccessToken?.let {
                                    saveAccessToken(context, it)
                                }

                                val retryResponse = RetrofitInstance.accountApi
                                    .getUserImage("profile/${email}.jpg", newAccessToken!!)
                                if (retryResponse.isSuccessful) {
                                    val bitmap = BitmapFactory.decodeStream(imageResponse.body()?.byteStream())
                                    _userImage.postValue(bitmap)
                                }

                                val cookie = refreshResponse.headers()["Set-Cookie"]
                                cookie?.split(";")?.forEach { cookie ->
                                    val cookiePair = cookie.split("=")
                                    if (cookiePair[0] == "refresh") {
                                        refreshToken = cookiePair[1].trim()
                                    }
                                }
                                saveRefreshToken(context, refreshToken!!)
                            } else {
                                reLogin()
                            }
                        }
                    }
                    else{
                        reLogin()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getEmailFromToken(token: String, secretKey: String): String? {
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(secretKey.toByteArray()) // JWT 토큰 서명에 사용된 비밀 키
                .parseClaimsJws(token)
                .body

            claims["username"] as String
        } catch (e: SignatureException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}