package com.example.doc_di.data.model

data class PillTaskModel(
    val pillName: String,
    val pillUnit: String,
    val pillAmount: String,
    val pillCycle: String,
    val startDate: String? = null,
    val takeTime: String? = null
)