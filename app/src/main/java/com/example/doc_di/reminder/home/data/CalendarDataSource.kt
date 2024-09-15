package com.example.doc_di.reminder.home.data

import com.example.doc_di.extension.toDate
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.reminder.home.model.CalendarModel
import java.util.Calendar
import java.util.Date

class CalendarDataSource {

    val today: Date
        get() {
            return Date()
        }

    fun getLastSelectedDate(dateString: String): Date {
        return dateString.toDate() ?: today
    }

    fun getData(startDate: Date = today, lastSelectedDate: Date): CalendarModel {
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        if (calendar.time.after(startDate)) {
            // 현재 날짜가 주의 시작일보다 이전일 경우, 이전 주로 조정
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
        }
        val firstDayOfWeek = calendar.time
        //val firstDayOfWeek = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val endDayOfWeek = calendar.time

        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toCalendarModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: Date, endDate: Date): List<Date> {
        val dateList = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        while (calendar.time <= endDate) {
            dateList.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return dateList
    }

    private fun toCalendarModel(
        dateList: List<Date>,
        lastSelectedDate: Date
    ): CalendarModel {
        return CalendarModel(
            selectedDate = toItemModel(lastSelectedDate, true),
            visibleDates = dateList.map {
                toItemModel(
                    date = it,
                    isSelectedDate = it.toFormattedDateString() == lastSelectedDate.toFormattedDateString()
                )
            }
        )
    }

    private fun toItemModel(date: Date, isSelectedDate: Boolean): CalendarModel.DateModel {
        return CalendarModel.DateModel(
            isSelected = isSelectedDate,
            isToday = isToday(date),
            date = date
        )
    }

    private fun isToday(date: Date): Boolean {
        val todayDate = today
        return date.toFormattedDateString() == todayDate.toFormattedDateString()
    }
}