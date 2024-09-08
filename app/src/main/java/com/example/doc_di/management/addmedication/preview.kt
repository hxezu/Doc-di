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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
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
import com.example.doc_di.etc.Routes
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
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }

    var isNameEntered by remember { mutableStateOf(false) }
    var isDoseEntered by remember { mutableStateOf(false) }
    var isRecurrenceSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }

    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(CalendarInformation(Calendar.getInstance())) }
    var selectedTimeIndices by remember { mutableStateOf(setOf<Int>()) }
    var lastSelectedIndex by remember { mutableStateOf<Int?>(null) }

    fun setTimeSelected(index: Int, isSelected: Boolean) {
        selectedTimeIndices = if (isSelected) {
            selectedTimeIndices + index
        } else {
            selectedTimeIndices - index
        }
        lastSelectedIndex = if (isSelected) index else lastSelectedIndex
    }

    fun addTime(time: CalendarInformation) {
        selectedTimes.add(time)
    }

    fun removeTime(time: CalendarInformation) {
        selectedTimes.remove(time)
    }

    // Check if the last TimerTextField is selected
    val isButtonEnabled = selectedTimes.isNotEmpty() && selectedTimeIndices.contains(selectedTimes.lastIndex)


    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.managementScreen.route) {
                                navController.popBackStack()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("저장",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White) },
                onClick = {
                    navController.navigate(Routes.addMedicationScreen.route)
                },
                icon = {
                    Icon(imageVector = Icons.Default.Check,
                        contentDescription = "Add",
                        tint = Color.White)
                },
                containerColor = MainBlue,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 0.dp,
                ),
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                shape = MaterialTheme.shapes.extraLarge
            )

        },
        floatingActionButtonPosition = FabPosition.Center,
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

            AddMedicationName(
                isNameEntered = isNameEntered,
                onNameChange = { name ->
                    isNameEntered = name.isNotEmpty()
                })
            Spacer(modifier = Modifier.padding(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DoseInputField(
                    isDoseEntered = isDoseEntered,
                    maxDose = 99,
                    onValueChange = { doseValue ->
                        isDoseEntered = doseValue.isNotEmpty()
                    })
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
                        setTimeSelected(index, true)
                    },
                    onDeleteClick = {
                        removeTime(selectedTimes[index])
                        setTimeSelected(index, false)
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                    isTimeSelected = selectedTimeIndices.contains(index)
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.padding(bottom = 70.dp),
                    onClick = {
                        if (isButtonEnabled) {
                            addTime(CalendarInformation(Calendar.getInstance()))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = if (isButtonEnabled) MainBlue else Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = isButtonEnabled
                ) {
                    Icon(imageVector = Icons.Default.Notifications,
                        contentDescription = "Add",
                        tint = if (isButtonEnabled) MainBlue else Color.Gray)
                    Text("시간 추가", color = if (isButtonEnabled) MainBlue else Color.Gray, modifier = Modifier.padding(start = 10.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
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