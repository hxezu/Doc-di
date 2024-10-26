package com.example.doc_di.reminder.util

enum class Recurrence {
    None,
    Daily,
    Weekly,
    Monthly,
}

fun getRecurrenceList(): List<Recurrence> {
    val recurrenceList = mutableListOf<Recurrence>()
    recurrenceList.add(Recurrence.None)
    recurrenceList.add(Recurrence.Daily)
    recurrenceList.add(Recurrence.Weekly)
    recurrenceList.add(Recurrence.Monthly)

    return recurrenceList
}