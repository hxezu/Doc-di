package com.example.doc_di.domain.chatbot.dto

import com.example.doc_di.domain.model.Pill

data class RasaDto(
    val recipient_id: String?,
    val text: String?,
    val buttons: List<RasaButtonDto>?,
    val custom: RasaCustomDto?,
    val medicineList: List<Pill>?
)
