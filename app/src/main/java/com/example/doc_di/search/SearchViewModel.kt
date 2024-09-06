package com.example.doc_di.search

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.PillInfo
import com.example.doc_di.domain.pill.PillsSearchRepository
import com.example.doc_di.domain.pill.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val pillsSearchRepository: PillsSearchRepository
) :ViewModel(){
    var showSearch = mutableStateListOf(false,false,false)
    var searchTitle = arrayOf("제품명 검색", "모양 검색", "사진 검색")

    private val selectedPill = mutableStateOf<Pill?>(null)

    private var options = mapOf<String, String>()

    private val _pills = MutableStateFlow<List<Pill>>(emptyList())
    val pills = _pills.asStateFlow()

    private val _pillInfo = MutableStateFlow<PillInfo>(PillInfo())
    val pillInfo = _pillInfo.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        searchPillsByOptions()
    }

    fun searchPillsByOptions(){
        viewModelScope.launch {
            _isLoading.value = true
            pillsSearchRepository.getPillSearchList(options).collectLatest { result ->
                _isLoading.value = false
                when(result){
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success -> {
                        Log.d("PillsViewModel", "Result.Success: ${result.data}")
                        result.data?.let { pillsList ->
                            _pills.update { pillsList }
                        }
                    }
                }
            }
        }
    }

    fun setPillInfo(name: String){
        viewModelScope.launch {
            _isLoading.value = true
            pillsSearchRepository.getPillInfo(name).collectLatest { result ->
                _isLoading.value = false
                when(result){
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success -> {
                        result.data?.let { pillInfo ->
                            _pillInfo.update { pillInfo }
                        }
                    }
                }
            }
        }
    }

    fun setSelectedPill (pill : Pill){
        selectedPill.value = pill
    }

    fun getSelectedPill (): Pill{
        return selectedPill.value!!
    }

    fun setOptions (queryParams: Map<String,String>){
        options = queryParams
    }

    fun resetPillInfo() {
        _pillInfo.value = PillInfo()
    }

    fun resetPills(){
        _pills.value = emptyList()
    }
}