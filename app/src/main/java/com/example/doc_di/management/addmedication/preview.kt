package com.example.doc_di.management.addmedication

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.management.addmedication.model.CalendarInformation
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.util.Recurrence
import java.util.Calendar
import java.util.Date


//현재 앱 실행 때 뜨는 화면
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreenUI(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel
){
    var text by rememberSaveable { mutableStateOf("") }
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(
        CalendarInformation(
            Calendar.getInstance())
    ) }
    var isRecurrenceSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }
    var selectedTimeIndex by remember { mutableStateOf(-1) }

    fun addTime(time: CalendarInformation) {
        selectedTimes.add(time)
    }
    fun removeTime(time: CalendarInformation) {
        selectedTimes.remove(time)
    }

    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(75.dp)
                    .padding(top = 25.dp, bottom = 16.dp),
                navigationIcon = {
                    FloatingActionButton(
                        onClick = {
                        },
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MainBlue
                        )
                    }
                },
                title = {},
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MainBlue,
                    titleContentColor = Color.Black
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxSize()
                .padding(paddingValues)  // Apply paddingValues here to avoid overlapping with the TopAppBar
                .padding(horizontal = 20.dp)  // Additional padding as needed
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
            ){
                Text(
                    text = "복용 약 추가",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }

            AddMedicationName()
            Spacer(modifier = Modifier.padding(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DoseInputField(maxDose = 99, onValueChange = { doseValue -> })
                RecurrenceDropdownMenu (
                    recurrence = { selectedRecurrence ->
                        recurrence = selectedRecurrence
                        isRecurrenceSelected = true
                    },
                    isRecurrenceSelected = isRecurrenceSelected)
            }

            Spacer(modifier = Modifier.padding(4.dp))
            EndDateTextField(
                onDateSelected = { selectedEndDate ->
                    endDate = selectedEndDate
                    isEndDateSelected = true
                },
                isEndDateSelected = isEndDateSelected
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                color = Color.Black,
                text = "복용 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                TimerTextField(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                    },
                    onDeleteClick = {
                        removeTime(selectedTimes[index])
                    },
                    isSelected = selectedTimeIndex == index,
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        addTime(CalendarInformation(Calendar.getInstance()))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    Text("시간 추가")
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                        .height(56.dp),
                    onClick = {
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Text(
                        text = "저장",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreenUIPreview( ){
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    AddMedicationScreenUI(navController = navController, btmBarViewModel = btmBarViewModel)
}