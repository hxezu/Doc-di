package com.example.doc_di.reminder.medication_reminder.utils

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.doc_di.R
import java.util.Calendar

@Composable
fun EndDatePickerDialog(
    shouldDisplay: Boolean,
    initialDateInMillis: Long,
    onConfirmClicked: (selectedDateInMillis: Long) -> Unit,
    dismissRequest: () -> Unit
) {
    val context = LocalContext.current

    if (shouldDisplay) {
        ShowCustomDatePickerDialog(
            context = context,
            initialDateInMillis = initialDateInMillis,
            onDateSelected = { selectedDateInMillis ->
                onConfirmClicked(selectedDateInMillis)
                dismissRequest()
            },
            onDismissRequest = dismissRequest
        )
    }
}

@Composable
fun ShowCustomDatePickerDialog(
    context: Context,
    initialDateInMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = remember { Calendar.getInstance().apply { timeInMillis = initialDateInMillis } }

    LaunchedEffect(Unit) {
        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerTheme, // Your custom style
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                onDateSelected(selectedCalendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setOnDismissListener {
            onDismissRequest()
        }

        datePickerDialog.show()
    }
}
