package com.example.doc_di.domain.model

import com.example.practice.data.model.Pill

data class Pills(
    val data: List<Pill>,
    val success: Boolean
)