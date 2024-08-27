package com.example.doc_di.domain.model

data class JoinDTO(
    val email : String,
    val password: String,
    val name: String,
    val sex: String,
    val birthday: String ,
    val height: Short,
    val weight: Short,
    val bloodType: String,
    val phoneNum: String
)
