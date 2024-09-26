package com.example.doc_di.app

import android.app.Application
import com.example.doc_di.domain.RetrofitInstance
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : Application()