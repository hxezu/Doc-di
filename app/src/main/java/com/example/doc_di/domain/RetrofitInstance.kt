package com.example.doc_di.domain

import com.example.doc_di.domain.login.LoginApi
import com.example.doc_di.domain.pillsearch.Api
import com.example.doc_di.domain.register.RegisterApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // 꽁제리너스
    //const val BASE_URL = "http://172.30.1.93:8080/"
    // 내 핫스팟
    const val BASE_URL = "http://192.168.99.189:8080/"

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
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
}