package com.example.doc_di.management

import android.adservices.topics.Topic
import android.annotation.SuppressLint
import android.os.Build
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManagementScreen(navController: NavController) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
            paddingValues ->
        CalendarApp(userId = 123)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarApp(userId : Int){
    var currentMonth by remember { mutableStateOf(YearMonth.now())}
    var selectedDate by remember { mutableStateOf(LocalDate.now())}
    var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var isDialogOpen by remember { mutableStateOf(false)}

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material.MaterialTheme.colors.background)){
            TopBar()
            Spacer(modifier = Modifier.height(16.dp))
            MonthNavigation(currentMonth, onPrevMonth = {
                currentMonth = currentMonth.minusMonths(1)
                selectedDate = if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(1)
            }, onNextMonth = {
                currentMonth = currentMonth.plusMonths(1)
                selectedDate = if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(1)
            })
            Spacer(modifier = Modifier.height(16.dp))
            CalendarView(currentMonth, selectedDate, onDateSelected = { date ->
                selectedDate = date
            }, onSwipeRight = {
                currentMonth = currentMonth.minusMonths(1)
                selectedDate = if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(1)

            }, onSwipeLeft = {
                currentMonth = currentMonth.plusMonths(1)
                selectedDate = if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(1)

            })


        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit
){
    Column (modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)) {
        WeekDayHeader()
        Spacer(modifier = Modifier.height(4.dp))
        DaysGrid(currentMonth, selectedDate, onDateSelected, onSwipeRight = {
            onSwipeRight()
        }, onSwipeLeft = {
            onSwipeLeft()
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
){
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalDays = lastDayOfMonth.dayOfMonth

    Column(modifier = Modifier.pointerInput(Unit){
        detectHorizontalDragGestures { _, dragAmount ->
            when {
                dragAmount > 0 -> onSwipeRight()
                dragAmount < 0 -> onSwipeLeft()
            }
        }
    }) {
        var day = 1
        for(week in 0 .. 5){
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayOfWeek in 0..6) {
                    if (week == 0 && dayOfWeek < firstDayOfWeek || day > totalDays) {
                        Box(modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f))
                    } else {
                        val date = currentMonth.atDay(day)
                        val isSelected = date == selectedDate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .background(
                                    color = if (isSelected) Color(0xFFE5FF7F) else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onDateSelected(date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                color =  androidx.compose.material.MaterialTheme.colors.onBackground
                            )
                        }
                        day++
                    }
                }
            }
        }
    }

}

@Composable
fun WeekDayHeader(){
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(modifier = Modifier.fillMaxWidth()) {
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = androidx.compose.material.MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun MonthNavigation(currentMonth: YearMonth, onPrevMonth: () -> Unit, onNextMonth : () -> Unit){
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(painter = painterResource(id = R.drawable.previous),
            contentDescription = "PrevMonth",
            tint = androidx.compose.material.MaterialTheme.colors.onBackground,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .padding(4.dp)
                .clickable { onPrevMonth() }
        )
        Text(
            text = currentMonth.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault()) + " " + currentMonth.year,
            fontSize = 16.sp,
            color = androidx.compose.material.MaterialTheme.colors.onBackground
        )
        Icon(
            painter = painterResource(id = R.drawable.next),
            contentDescription = "NextMonth",
            tint = androidx.compose.material.MaterialTheme.colors.onBackground,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .padding(4.dp)
                .clickable { onNextMonth() }
        )
    }
}

@Composable
fun TopBar(){
    val context = LocalContext.current
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp)
            .background(androidx.compose.material.MaterialTheme.colors.background)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Calender",
                fontSize = 18.sp,
                color = androidx.compose.material.MaterialTheme.colors.onBackground
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CManagementScreenPreview(){
    val navController = rememberNavController()
    ManagementScreen(navController = navController)
}