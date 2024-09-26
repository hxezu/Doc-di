package com.example.doc_di.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.domain.chatbot.ChatBotApi
import com.example.doc_di.domain.chatbot.ChatBotClientDto
import com.example.doc_di.domain.chatbot.ChatBotImpl
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatBotViewModel(private val chatBotImpl: ChatBotImpl) : ViewModel() {

    private val _chatList = MutableLiveData<List<Chat>>(emptyList())
    val chatList: LiveData<List<Chat>> get() = _chatList

    private var chatId = 0

    fun sendMessage(message: String, userEmail: String) {
        addChat(message, isUser = true)

        viewModelScope.launch {
            try {
                val chatBotClientDto = ChatBotClientDto(
                    email = userEmail,
                    message = message
                )
                val response = chatBotImpl.chatWithRasa(chatBotClientDto)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val botMessage = responseBody?.data ?: "챗봇으로부터 응답이 없습니다."
                    addChat(botMessage, isUser = false)
                } else {
                    val errorMessage = "오류: ${response.code()} - ${response.message()}"
                    addChat(errorMessage, isUser = false)
                }
            } catch (e: Exception) {
                val exceptionMessage = "예외 발생: ${e.message}"
                addChat(exceptionMessage, isUser = false)
            }
        }
    }

    private fun addChat(message: String, isUser: Boolean) {
        val newChat = Chat(
            id = ++chatId,
            message = message,
            time = getCurrentTime(),
            isUser = isUser
        )
        _chatList.value = _chatList.value?.plus(newChat)
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
