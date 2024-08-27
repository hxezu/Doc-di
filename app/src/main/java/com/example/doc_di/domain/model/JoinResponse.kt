package com.example.doc_di.domain.model

import android.service.autofill.UserData

data class JoinResponse(
    val status: Int,
    val message: String,
    val data: UserData
)