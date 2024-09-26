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
import com.example.doc_di.domain.chatbot.ChatRepository
import com.example.doc_di.domain.chatbot.RasaDto
import com.example.doc_di.domain.model.Message
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatBotViewModel(
    private val chatRepository: ChatRepository,
    private val chatBotImpl: ChatBotImpl
) : ViewModel() {

    private val _chatList = MutableLiveData<List<Chat>>(emptyList())
    val chatList: LiveData<List<Chat>> get() = _chatList

    private var chatId = 0

    fun getChatById(email: String, chatId: Int): LiveData<Chat?> {
        val chat = MutableLiveData<Chat?>()
        chat.value = chatRepository.getChatById(email, chatId)
        return chat
    }

    fun loadChats(email: String) {
        val chats = chatRepository.getChatsByUser(email)
        _chatList.value = chats
    }

    fun createNewChat(email: String): String? {
        val newChat = chatRepository.createNewChat(email)
        loadChats(email) // Reload chats after creating a new one
        return newChat.id.toString()
    }

    fun sendMessage(email: String, message: String, chatId: Int) {
        addChat(email, message, isUser = true, chatId)

        viewModelScope.launch {
            try {
                val chatBotClientDto = ChatBotClientDto(email = email, message = message)
                Log.d("ChatBotViewModel", "Sending message: $chatBotClientDto")
                val response = chatBotImpl.chatWithRasa(chatBotClientDto)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val rasaDto = Gson().fromJson(responseBody.data, RasaDto::class.java)

                        // Rasa 서버의 응답에 따라 처리
                        when (rasaDto.action) {
                            "DB_SEARCH" -> {
                                // DB 검색 관련 작업 처리
                                addChat(email, "DB 검색 결과: ${rasaDto.data}", isUser = false, chatId)
                            }
                            "PLAIN" -> {
                                // 단순 메시지 응답 처리
                                addChat(email, rasaDto.message, isUser = false, chatId)
                            }
                            else -> {
                                // 기타 액션 처리
                                addChat(email, "알 수 없는 액션: ${rasaDto.action}", isUser = false, chatId)
                            }
                        }
                    } else {
                        addChat(email, "챗봇으로부터 응답이 없습니다.", isUser = false, chatId)
                    }
                } else {
                    addChat(email, "오류 발생: ${response.code()} - ${response.message()}", isUser = false, chatId)
                    Log.e("ChatBotViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                addChat(email, "예외 발생: ${e.message}", isUser = false, chatId)
                Log.e("ChatBotViewModel", "Exception occurred: ${e.message}")
            }
        }
    }

    private fun addChat(email: String, message: String, isUser: Boolean, chatId: Int) {
        val chat = chatRepository.getChatById(email, chatId) ?: return // Use the size of existing chats

        val messageId = chat.messages.size + 1 // Simple way to generate a unique ID based on the current size
        val newMessage = Message(
            id = messageId, // Pass the generated ID here
            content = message,
            time = getCurrentTime(),
            isUser = isUser
        )

        chat.messages.add(newMessage) // Add the new message to the chat
        chatRepository.saveChat(email, chatId, chat) // Save the updated chat
        _chatList.value = chatRepository.getChatsByUser(email)
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
