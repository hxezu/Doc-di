package com.example.doc_di.login

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.account.AccountDTO
import com.example.doc_di.domain.review.SearchHistory
import com.example.doc_di.etc.Routes
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.launch
import java.security.SignatureException
import java.util.Date

class UserViewModel: ViewModel(){
    val secretKey =
        "af86bed1b346575a174c4026addb078151bd2484be48655c69bb8155f7d3c1f3cafd0bf5f2dab7ad665ce4f9e42df366d421a9dc041d78e377fab127d237b988"

    private val _userInfo = MutableLiveData<AccountDTO?>()
    val userInfo: LiveData<AccountDTO?> get() = _userInfo

    private val _userImage = MutableLiveData<Bitmap?>()
    val userImage: LiveData<Bitmap?> get() = _userImage

    private val _pastHistory = MutableLiveData<List<SearchHistory>?>()
    val pastHistory: LiveData<List<SearchHistory>?> get() = _pastHistory

    suspend fun fetchUser(context: Context, navController: NavController, onComplete: () -> Unit) {
        viewModelScope.launch {
            val reLogin = {
                navController.navigate(Routes.login.route) {
                    popUpTo(Routes.login.route) {
                        inclusive = true
                    }
                }
            }

            try {
                val accessToken = checkAccessAndReissue(context, navController)
                if (accessToken != null) {
                    val email = getEmailFromToken(accessToken) ?: return@launch

                    val pillHistoryResponse = RetrofitInstance.pillApi.getPillHistory(accessToken, email)
                    if (pillHistoryResponse.isSuccessful){
                        _pastHistory.postValue(pillHistoryResponse.body()?.data)
                    }
                    else{
                        reLogin()
                    }

                    val userInfoResponse = RetrofitInstance.accountApi.getUserInfo(email, accessToken)
                    if (userInfoResponse.isSuccessful) {
                        _userInfo.postValue(userInfoResponse.body()!!.data)
                    }
                    else{
                        reLogin()
                    }

                    val imageResponse = RetrofitInstance.accountApi.getUserImage("profile/${email}.jpg", accessToken)
                    if (imageResponse.isSuccessful) {
                        val bitmap = BitmapFactory.decodeStream(imageResponse.body()?.byteStream())
                        _userImage.postValue(bitmap)
                    }
                    else{
                        reLogin()
                    }
                    onComplete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun checkAccessAndReissue(context: Context, navController: NavController): String? {
        val reLogin = {
            navController.navigate(Routes.login.route) {
                popUpTo(Routes.login.route) {
                    inclusive = true
                }
            }
        }

        val sharedPreferences = getEncryptedSharedPreferences(context)
        var accessToken = sharedPreferences.getString("access_token", null)
        var refreshToken = sharedPreferences.getString("refresh_token", null)
        if (accessToken.isNullOrEmpty()) {
            reLogin()
            return null
        }

        val email = getEmailFromToken(accessToken)
        if (email == null) {
            reLogin()
            return null
        }

        if (!isTokenExpired(accessToken)) {
            return accessToken
        } else if (!refreshToken.isNullOrEmpty()) {
            val refreshResponse = RetrofitInstance.accountApi.reissueToken()
            if (refreshResponse.isSuccessful) {
                val newAccessToken = refreshResponse.headers()["access"]
                newAccessToken?.let {
                    saveAccessToken(context, it)
                    accessToken = it
                }

                val cookie = refreshResponse.headers()["Set-Cookie"]
                cookie?.split(";")?.forEach { cookie ->
                    val cookiePair = cookie.split("=")
                    if (cookiePair[0] == "refresh") {
                        refreshToken = cookiePair[1].trim()
                    }
                }
                saveRefreshToken(context, refreshToken!!)
                return accessToken
            } else {
                reLogin()
                return null
            }
        } else {
            reLogin()
            return null
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(secretKey.toByteArray())
                .parseClaimsJws(token)
                .body

            val expiration: Date = claims.expiration
            expiration.before(Date())
        } catch (e: SignatureException) {
            e.printStackTrace()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }
    private fun getEmailFromToken(token: String): String? {
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

    fun getPillHistory(email: String, context: Context, navController: NavController){
        viewModelScope.launch {
            val accessToken = checkAccessAndReissue(context, navController)
            val pillHistoryResponse = RetrofitInstance.pillApi.getPillHistory(accessToken!!, email)
            if (pillHistoryResponse.isSuccessful){
                _pastHistory.postValue(pillHistoryResponse.body()?.data)
            }
        }
    }

    fun deletePillHistory(id: Int, context: Context, navController: NavController){
        viewModelScope.launch {
            val accessToken = checkAccessAndReissue(context, navController)
            val deleteHistoryResponse = RetrofitInstance.pillApi.deleteHistory(accessToken!!, id)
            if (deleteHistoryResponse.isSuccessful){
                getPillHistory(getEmailFromToken(accessToken)!!, context, navController)
            }
        }
    }

    fun clearUserData() {
        _userInfo.postValue(null)
        _userImage.postValue(null)
    }
}