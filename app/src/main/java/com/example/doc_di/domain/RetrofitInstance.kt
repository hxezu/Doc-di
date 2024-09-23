package com.example.doc_di.domain

import com.example.doc_di.domain.account.AccountApi
import com.example.doc_di.domain.login.LoginApi
import com.example.doc_di.domain.pill.PillApi
import com.example.doc_di.domain.register.RegisterApi
import com.example.doc_di.domain.reminder.ReminderApi
import com.example.doc_di.domain.resetpw.ResetApi
import com.example.doc_di.domain.review.ReviewApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    //const val BASE_URL = "http://192.168.99.189:8080/"
    const val BASE_URL = "http://192.168.0.7:8080/"
    //const val BASE_URL = "http://192.168.64.25:8080/"

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .cookieJar(RefreshCookieJar())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    val pillApi: PillApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(PillApi::class.java)

    val registerApi: RegisterApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(RegisterApi::class.java)

    val reminderApi: ReminderApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(ReminderApi::class.java)

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

    val reviewApi: ReviewApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(ReviewApi::class.java)

    val resetApi: ResetApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()
        .create(ResetApi::class.java)
}