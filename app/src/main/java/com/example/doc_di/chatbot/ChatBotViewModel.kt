package com.example.doc_di.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.domain.chatbot.dto.ChatBotClientDto
import com.example.doc_di.domain.chatbot.ChatBotImpl
import com.example.doc_di.domain.chatbot.ChatRepository
import com.example.doc_di.domain.model.Message
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
                val chatBotClientDto = ChatBotClientDto(sender = email, message = message)
                Log.d("ChatBotViewModel", "Sending message: $chatBotClientDto")
                val response = chatBotImpl.chatWithRasa(chatBotClientDto)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    println("response: $responseBody")
                    if (responseBody != null && responseBody.data != null) {
                        val rasaDtoList = responseBody.data

                        for (rasaDto in rasaDtoList) {
                            // 응답 메시지 추가
                            rasaDto.text?.let { text ->
                                addChat(email, text, isUser = false, chatId)
                            }
                            // 버튼 처리 예시 (필요에 따라 처리)
                            rasaDto.buttons?.let { buttons ->
                                buttons.forEach { button ->
                                    addChat(email, "버튼: ${button.title}", isUser = false, chatId)
                                }
                            }
                        }
                        Log.d("ChatBotViewModel", "Chat list after response: ${_chatList.value}")
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

        val updatedChatList = _chatList.value?.toMutableList()?.apply {
            // 같은 chatId를 가진 Chat 객체를 찾아서 업데이트
            val index = indexOfFirst { it.id == chatId }
            if (index != -1) {
                set(index, chat)  // 리스트에서 해당 chat 객체를 업데이트
            }
        }

        _chatList.value = updatedChatList ?: listOf(chat) // Ensure it's not null
        chatRepository.saveChat(email, chatId, chat) // Save the updated chat, not add a new one
        Log.d("ChatBotViewModel", "Updated chat list: ${_chatList.value}")
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
