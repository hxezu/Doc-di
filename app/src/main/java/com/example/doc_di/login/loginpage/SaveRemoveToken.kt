package com.example.doc_di.login.loginpage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun saveAccessToken(context: Context, token: String) {
    val sharedPreferences = getEncryptedSharedPreferences(context)
    sharedPreferences.edit().putString("access_token", token).apply()
}

fun saveRefreshToken(context: Context, token: String) {
    val sharedPreferences = getEncryptedSharedPreferences(context)
    sharedPreferences.edit().putString("refresh_token", token).apply()
}

fun removeAccessToken(context: Context){
    val sharedPreferences = getEncryptedSharedPreferences(context)
    sharedPreferences.edit().remove("access_token").apply()
}

fun removeRefreshToken(context: Context){
    val sharedPreferences = getEncryptedSharedPreferences(context)
    sharedPreferences.edit().remove("refresh_token").apply()
}
