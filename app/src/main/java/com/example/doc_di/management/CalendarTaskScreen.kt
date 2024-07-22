package com.example.doc_di.management

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.data.model.Task
import com.example.doc_di.data.model.TaskModel
import com.example.doc_di.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarTaskScreen(userId: Int, date: String? = null,  viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.taskList.collectAsState()

    LaunchedEffect(userId, date) {
        viewModel.getTaskListByDate(userId, date, onSuccess = {

        }, onError = {})
    }

    Column{
        when {
            tasks == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            tasks!!.isEmpty() -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                }

            }
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(tasks!!) { task ->
                        TaskCard(task = task, onDelete = {
                            viewModel.deleteTask(userId, task.task_id, onSuccess = {
                                viewModel.getTaskListByDate(userId, date, onSuccess = {

                                }, onError = {})
                            }, onError = {

                            })
                        })
                    }
                }
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCard(task: Task, onDelete : (Task) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { /* Handle click if needed */ } ,
        backgroundColor = Color(0xFF4DF6E9),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = task.task_detail.title ?: "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDateTime(task.task_detail.date, task.task_detail.time) ?: "",
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(6.dp)).padding(4.dp),
                    color = Color.Black,
                    fontSize = 12.sp,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = task.task_detail.description ?: "",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE5FF7F))
                        .padding(4.dp)
                        .clickable { onDelete(task) }
                )
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(date: String?, time: String?): String {
    return try {
        val inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
        val outputDateFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)
        val localDate = LocalDate.parse(date, inputDateFormatter)
        val formattedDate = localDate.format(outputDateFormatter)

        val formattedTime = if (time != null) {
            val inputTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            val localTime = LocalTime.parse(time, inputTimeFormatter)
            localTime.format(inputTimeFormatter)
        } else {
            ""
        }

        if (formattedTime.isNotEmpty()) {
            "$formattedDate, $formattedTime"
        } else {
            formattedDate
        }
    } catch (e: Exception) {
        date ?: ""
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    // Task 객체를 생성
    val task = Task(
        task_id = 1,
        task_detail = TaskModel(
            title = "Sample Task",
            description = "This is a sample description",
            date = "22/07/2024",
            time = "10:00 AM"
        )
    )
    TaskCard(
        task = task,
        onDelete = {
            // onDelete 콜백 구현
            // 예: 로그를 출력하거나 UI를 업데이트
        }
    )
}
