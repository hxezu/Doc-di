package com.example.doc_di.management.home.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.doc_di.analytics.AnalyticsEvents
import com.example.doc_di.extension.toFormattedDateShortString
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.extension.toFormattedKoreanDateString
import com.example.doc_di.management.home.data.CalendarDataSource
import com.example.doc_di.management.home.model.CalendarModel
import com.example.doc_di.ui.theme.MainBlue
import java.util.Calendar
import java.util.Date

@Composable
fun DatesHeader(
    lastSelectedDate: String,
    onDateSelected: (CalendarModel.DateModel) -> Unit, // Callback to pass the selected date){}
    logEvent: (String) -> Unit
) {
    val dataSource = CalendarDataSource()
    var calendarModel by remember {
        mutableStateOf(
            dataSource.getData(lastSelectedDate = dataSource.getLastSelectedDate(lastSelectedDate))
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        DateHeader(
            data = calendarModel,
            onPrevClickListener = { startDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the startDate-1 from the visibleDates)
                val calendar = Calendar.getInstance()
                calendar.time = startDate

                calendar.add(Calendar.DAY_OF_YEAR, -2) // Subtract one day from startDate
                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
                logEvent.invoke(AnalyticsEvents.HOME_CALENDAR_PREVIOUS_WEEK_CLICKED)
            },
            onNextClickListener = { endDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the endDate+2 from the visibleDates)
                val calendar = Calendar.getInstance()
                calendar.time = endDate

                calendar.add(Calendar.DAY_OF_YEAR, 2)
                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarModel.selectedDate.date)
                logEvent.invoke(AnalyticsEvents.HOME_CALENDAR_NEXT_WEEK_CLICKED)
            }
        )
        DateList(
            data = calendarModel,
            onDateClickListener = { date ->
                calendarModel = calendarModel.copy(
                    selectedDate = date,
                    visibleDates = calendarModel.visibleDates.map {
                        it.copy(
                            isSelected = it.date.toFormattedDateString() == date.date.toFormattedDateString()
                        )
                    }
                )
                onDateSelected(date)
            }
        )
    }
}

@Composable
fun DateHeader(
    data: CalendarModel,
    onPrevClickListener: (Date) -> Unit,
    onNextClickListener: (Date) -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 25.dp, bottom = 25.dp, start = 20.dp),
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = if (data.selectedDate.isToday) {
                "오늘"
            } else {
                data.selectedDate.date.toFormattedKoreanDateString()
            },
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                tint = Color.Black,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                tint = Color.Black,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun DateList(
    data: CalendarModel,
    onDateClickListener: (CalendarModel.DateModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // 항목 사이의 간격 설정
        verticalAlignment = Alignment.CenterVertically
    ) {
        data.visibleDates.forEach { date ->
            DateItem(date, onDateClickListener)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateItem(
    date: CalendarModel.DateModel,
    onClickListener: (CalendarModel.DateModel) -> Unit,
) {
    Column {
        Text(
            text = date.day, // day "월", "화"
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = if (date.isSelected) MainBlue else MaterialTheme.colorScheme.outline
        )
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp),
            onClick = { onClickListener(date) },
            colors = cardColors(
                containerColor = Color.Transparent
            ),
        ) {
            Column(
                modifier = Modifier
                    .width(30.dp)
                    .height(42.dp)
                    .fillMaxSize()
                , // Fill the available size in the Column
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Text(
                    text = date.date.toFormattedDateShortString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (date.isSelected) {
                        MainBlue
                    } else {
                        Color.Black
                    },
                    fontWeight = if (date.isSelected) {
                        FontWeight.Medium
                    } else {
                        FontWeight.Normal
                    }
                )
            }
        }
    }
}