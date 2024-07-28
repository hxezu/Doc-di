package com.example.doc_di.management

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.doc_di.domain.model.PillTaskRequest
import com.example.doc_di.viewmodel.TaskViewModel
import com.example.doc_di.domain.model.PillTaskModel
import java.util.Calendar

@Composable
fun AddPillScreen(
    navController: NavController,
    userId : Int,
    startDate : String,
    takeTime : String,
    viewModel: TaskViewModel = hiltViewModel(),
    onSaveTask: () -> Unit
) {
    var pillName by remember { mutableStateOf("") }
    var pillUnit by remember { mutableStateOf("") }
    var pillAmount by remember { mutableStateOf("") }
    var pillCycle by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(startDate) }
    var takeTime by remember { mutableStateOf(takeTime) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "복용 약 추가") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text= "제품명",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            OutlinedTextField(
                value = pillName,
                onValueChange = { pillName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = { Text("제품명") },
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text= "투약 횟수",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            OutlinedTextField(
                value = pillUnit,
                onValueChange = { pillUnit = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text("단위") }
            )

            OutlinedTextField(
                value = pillAmount,
                onValueChange = { pillAmount = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text("1회 투약량")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = pillCycle,
                onValueChange = { pillCycle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text("1회 투약횟수") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text= "투약 시작 날짜",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text("투약 시작 날짜") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showDatePicker(context) { selectedDate ->
                            startDate = selectedDate
                        }
                    }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text= "투약 시간",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            OutlinedTextField(
                value = takeTime,
                onValueChange = { takeTime = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                label = { Text("투약 시간") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showTimePicker(context) { selectedTime ->
                            takeTime = selectedTime
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Select Time")
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text= "투약 알림",
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Cancel")
                }

                Button(
                    onClick = {
                        viewModel.storeTask(PillTaskRequest(userId, PillTaskModel(pillName, pillUnit, pillAmount, pillCycle, startDate, takeTime)), onSuccess = {
                            viewModel.getTaskListByDate(userId, date = startDate, onSuccess = {
                                onSaveTask()
                                navController.popBackStack()
                            }, onError = {
                                navController.popBackStack()
                            })
                        }, onError = {
                            navController.popBackStack()
                        })
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected("$selectedYear-${selectedMonth + 1}-$selectedDay")
    }, year, month, day).show()
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        onTimeSelected("$selectedHour:$selectedMinute")
    }, hour, minute, true).show()
}