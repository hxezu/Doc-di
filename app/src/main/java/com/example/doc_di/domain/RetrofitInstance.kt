package com.example.doc_di.domain

import com.example.doc_di.domain.account.AccountApi
import com.example.doc_di.domain.login.LoginApi
import com.example.doc_di.domain.pillsearch.Api
import com.example.doc_di.domain.register.RegisterApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // 꽁제리너스
    //const val BASE_URL = "http://172.30.1.62:8080/"
    // 내 핫스팟
    const val BASE_URL = "http://192.168.99.189:8080/"

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .cookieJar(RefreshCookieJar())
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    val api: Api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(Api::class.java)

    val registerApi: RegisterApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(RegisterApi::class.java)

    val loginApi: LoginApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(LoginApi::class.java)

    val accountApi: AccountApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(AccountApi::class.java)
}