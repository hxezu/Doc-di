package com.example.doc_di.util

enum class Recurrence {
    Daily,
    Weekly,
    Monthly,
    None
}

fun getRecurrenceList(): List<Recurrence> {
    val recurrenceList = mutableListOf<Recurrence>()
    recurrenceList.add(Recurrence.Daily)
    recurrenceList.add(Recurrence.Weekly)
    recurrenceList.add(Recurrence.Monthly)
    recurrenceList.add(Recurrence.None)

    return recurrenceList
}