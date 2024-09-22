package com.example.doc_di.domain.reminder

import com.example.doc_di.domain.model.Booked


data class BookedResponse(
    val success: Boolean,
    val data: List<Booked>
)