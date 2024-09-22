package com.example.doc_di.reminder.home.utils

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

class MultiFabItem(
    val iconRes: Int,
    val label: String,
    val labelColor: Color,
    val selectedDate: LocalDate?
)