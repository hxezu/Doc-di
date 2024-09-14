package com.example.doc_di.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.doc_di.R
import com.example.doc_di.domain.model.Reminder
import com.example.doc_di.extension.toFormattedDateString
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.abs

const val HOUR_MINUTE_FORMAT = "h:mm a"
@Composable
fun getTimeRemaining(reminder: Reminder): String {
    val currentTime = Calendar.getInstance().time
    val dateBefore = reminder.medicationTime
    val timeDiff = abs(currentTime.time - dateBefore.time)

    // If the medication is scheduled for a future date, display days remaining
    if (reminder.medicationTime.toFormattedDateString() != reminder.endDate.toFormattedDateString()) {
        val daysRemaining = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS) + 1L
        return stringResource(id = R.string.time_remaining, daysRemaining, "days")
    }

    // If the medication is scheduled for today, calculate time remaining in hours and minutes
    val hoursRemaining = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS)
    val minutesRemaining = TimeUnit.MINUTES.convert(timeDiff, TimeUnit.MILLISECONDS)
    return when {
        hoursRemaining > 1 -> stringResource(id = R.string.time_remaining, hoursRemaining, "hours")
        minutesRemaining > 1 -> stringResource(id = R.string.time_remaining, minutesRemaining, "days")
        else -> "Take your dose now"
    }
}