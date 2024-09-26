package com.example.doc_di.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.domain.chatbot.ChatBotApi
import com.example.doc_di.domain.chatbot.ChatBotClientDto
import com.example.doc_di.domain.chatbot.ChatBotImpl
import com.example.doc_di.domain.chatbot.RasaDto
import com.google.gson.Gson
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
                val chatBotClientDto = ChatBotClientDto(email = userEmail, message = message)
                println("chatBotClientDto: $chatBotClientDto")
                val response = chatBotImpl.chatWithRasa(chatBotClientDto)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val rasaDto = Gson().fromJson(responseBody.data, RasaDto::class.java)

                        // Now you can access the properties of rasaDto
                        when (rasaDto.action) {
                            "DB_SEARCH" -> {
                                // Handle DB_SEARCH action
                            }
                            "PLAIN" -> {
                                // Handle PLAIN action
                            }
                            // Add more cases as needed
                        }
                    }else{
                        addChat("No response from the chatbot.", isUser = false)
                    }
                } else {
                    addChat("Error: ${response.code()} - ${response.message()}", isUser = false)
                    Log.e("ChatBotApi", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                addChat("Exception occurred: ${e.message}", isUser = false)
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
