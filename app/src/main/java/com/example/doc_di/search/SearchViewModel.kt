package com.example.doc_di.search

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.RetrofitInstance
import com.example.doc_di.domain.model.Pill
import com.example.doc_di.domain.model.PillInfo
import com.example.doc_di.domain.pill.PillsSearchRepository
import com.example.doc_di.domain.pill.Result
import com.example.doc_di.domain.pill.SearchHistoryDto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class SearchViewModel(
    private val pillsSearchRepository: PillsSearchRepository,
) : ViewModel() {
    var showSearch = mutableStateListOf(false, false, false)
    var searchTitle = arrayOf("제품명 검색", "모양 검색", "사진 검색")

    private val selectedPill = mutableStateOf<Pill?>(null)

    private var options = mapOf<String, String>()

    private val _pills = MutableStateFlow<List<Pill>>(emptyList())
    val pills = _pills.asStateFlow()

    private val _pillInfo = MutableStateFlow<PillInfo>(PillInfo())
    val pillInfo = _pillInfo.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun searchPillsByOptions() {
        viewModelScope.launch {
            _isLoading.value = true
            pillsSearchRepository.getPillSearchList(options).collectLatest { result ->
                _isLoading.value = false
                when (result) {
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

    fun searchPillsByImage(context: Context, bitmap: Bitmap){
        viewModelScope.launch{
            _isLoading.value = true
            val pillImage = File(context.cacheDir, "pillImage.jpg")
            FileOutputStream(pillImage).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), pillImage)
            val imagePart = MultipartBody.Part.createFormData("image", pillImage.name, requestFile)

            val imageSearchResponse = RetrofitInstance.pillApi.getPillListByImage(imagePart)
            if (imageSearchResponse.code() == 200){
                val pillList = imageSearchResponse.body()!!.data
                _pills.update { pillList }
            }
            _isLoading.value = false
        }
    }

    fun setSelectedPillByPillName(pillName: String){
        val option = mutableMapOf<String, String>()
        option["name"] = pillName
        setOptions(option)
        searchPillsByOptions()
    }

    fun setPillInfo(searchHistoryDto: SearchHistoryDto) {
        viewModelScope.launch {
            _isLoading.value = true
            pillsSearchRepository.getPillInfo(searchHistoryDto).collectLatest { result ->
                _isLoading.value = false
                when (result) {
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

    fun setSelectedPill(pill: Pill) {
        selectedPill.value = pill
    }

    fun getSelectedPill(): Pill {
        return selectedPill.value!!
    }

    fun setOptions(queryParams: Map<String, String>) {
        options = queryParams
    }

    fun resetPillInfo() {
        _pillInfo.value = PillInfo()
    }

    fun resetPills() {
        _pills.value = emptyList()
    }
}