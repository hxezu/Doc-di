package com.example.doc_di.management.addschedule

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.extension.toFormattedDateString
import com.example.doc_di.management.addmedication.model.CalendarInformation
import com.example.doc_di.ui.theme.MainBlue
import com.example.doc_di.util.Department
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreenUI(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel
){
    var isRecurring by rememberSaveable { mutableStateOf(false) }  // Add state for toggle
    var department by rememberSaveable { mutableStateOf(Department.InternalMedicine.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    val selectedTimes = rememberSaveable(saver = CalendarInformation.getStateListSaver()) { mutableStateListOf(
        CalendarInformation(
            Calendar.getInstance())
    ) }

    var isHospitalEntered by remember { mutableStateOf(false) }
    var isDoctorEntered by remember { mutableStateOf(false) }
    var isDepartmentSelected by remember { mutableStateOf(false) }
    var isTimeSelected by remember { mutableStateOf(false) }
    var isEndDateSelected by remember { mutableStateOf(false) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp),
                verticalAlignment = CenterVertically // Aligns the image and text vertically in the center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hospitalemoji),
                    contentDescription = "Icon",
                    modifier = Modifier.size(30.dp) // Adjust size if necessary
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "진료 일정 추가",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                )
            }

            AddHospitalName(
                isHospitalEntered = isHospitalEntered,
                onHospitalChange = { hospital ->
                    isHospitalEntered = hospital.isNotEmpty()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            AddDoctorName(
                isDoctorEntered = isDoctorEntered,
                onDoctorChange = { doctor ->
                    isDoctorEntered = doctor.isNotEmpty()
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            DepartmentDropdownMenu(
                department = { selectedDepartment ->
                    department = selectedDepartment
                    isDepartmentSelected = true
                },
                isDepartmentSelected = isDepartmentSelected
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                color = Color.Black,
                text = "진료 시간",
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                TimerTextField(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                        isTimeSelected = true
                    },
                    onDeleteClick = {
                        removeTime(selectedTimes[index])
                    },
                    logEvent = {
                        //viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                    },
                    isTimeSelected = isTimeSelected
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = Color.Black,
                    text = "정기 진료",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.padding(end = 20.dp),
                    checked = isRecurring,
                    onCheckedChange = { checked -> isRecurring = checked },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MainBlue,
                        checkedTrackColor = MainBlue.copy(alpha = 0.5f)
                    )
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))

            if (isRecurring) {
                EndDateTextField (
                    onDateSelected = { selectedEndDate ->
                    endDate = selectedEndDate
                    isEndDateSelected = true
                },
                    isEndDateSelected = isEndDateSelected
            )
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}



@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreenUIPreview( ){
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    AddScheduleScreenUI(navController = navController, btmBarViewModel = btmBarViewModel)
}