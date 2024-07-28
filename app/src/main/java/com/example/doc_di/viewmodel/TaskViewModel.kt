package com.example.doc_di.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.doc_di.data.model.Task
import com.example.doc_di.domain.model.PillTaskRequest
import com.example.doc_di.domain.usecases.DeleteTaskUseCase
import com.example.doc_di.domain.usecases.GetTaskListUseCase
import com.example.doc_di.domain.usecases.StoreTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val storeTaskUseCase: StoreTaskUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _task = MutableStateFlow<List<Task>?>(null)
    val taskList: StateFlow<List<Task>?> = _task

    fun storeTask(pillTaskRequest: PillTaskRequest, onSuccess: () -> Unit, onError: () -> Unit)  {
        viewModelScope.launch {
            try {
                storeTaskUseCase.invoke(pillTaskRequest)
                onSuccess()
            }catch (e : Exception){
                onError()
                Log.i("--ViewModel--", "AddTaskList >> ${e.message}")
            }
        }
    }

    fun getTaskListByDate(userId: Int, date: String? = null, onSuccess: (List<Task>) -> Unit, onError: (String) -> Unit)  {
        viewModelScope.launch {
            try {
                val tasks = getTaskListUseCase(userId)
                _task.value = filterTasksByDate(tasks, date)
                onSuccess(tasks)
            } catch (e: Exception) {
                Log.i("--ViewModel--", "getTaskList >> ${e.message}")
                onError(e.message ?: "Failed to get user tasks")
            }
        }
    }

    fun getTaskList(userId: Int, onSuccess: (List<Task>) -> Unit, onError: (String) -> Unit)  {
        viewModelScope.launch {
            try {
                val tasks = getTaskListUseCase(userId)
                _task.value = tasks
                onSuccess(tasks)
            } catch (e: Exception) {
                Log.i("--ViewModel--", "getTaskList >> ${e.message}")
                onError(e.message ?: "Failed to get user tasks")
            }
        }
    }

    fun deleteTask(userId: Int, taskId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase.invoke(userId, taskId)
                onSuccess()
            }catch (e : Exception){
                Log.i("--ViewModel--", "getTaskList >> ${e.message}")
                onError(e.message.toString())
            }
        }
    }

    private fun filterTasksByDate(tasks: List<Task>, date: String?): List<Task> {
        return if (date.isNullOrEmpty()) {
            Log.i("--ViewModel--", "filter task >> data is null")
            tasks
        } else {
            Log.i("--ViewModel--", "filter task >> $date  ${tasks.size}")
            tasks.filter { it.task_detail.startDate == date }
        }
    }
}
