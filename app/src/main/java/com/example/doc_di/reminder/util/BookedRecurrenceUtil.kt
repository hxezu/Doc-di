package com.example.doc_di.reminder.util

enum class BookedRecurrence {
    Daily,
    Weekly,
    Monthly
}

fun getBookedRecurrenceList(): List<BookedRecurrence> {
    val bookedRecurrenceList = mutableListOf<BookedRecurrence>()
    bookedRecurrenceList.add(BookedRecurrence.Daily)
    bookedRecurrenceList.add(BookedRecurrence.Weekly)
    bookedRecurrenceList.add(BookedRecurrence.Monthly)

    return bookedRecurrenceList
}