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

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> get() = _messages

    // 채팅 ID로 특정 채팅을 가져오는 메서드
    fun getChatById(email: String, chatId: Int): LiveData<Chat?> {
        val chat = MutableLiveData<Chat?>()
        viewModelScope.launch {
            chat.value = chatRepository.getChatById(email, chatId)
        }
        return chat
    }

    fun deleteChat(email: String, chatId: Int) {
        viewModelScope.launch {
            try {
                chatRepository.deleteChat(chatId) // Firestore에서 대화 삭제
                loadChats(email) // 필요한 경우 사용자 채팅 목록 갱신
            } catch (e: Exception) {
                Log.e("ChatBotViewModel", "Error deleting chat: ${e.message}")
            }
        }
    }

    // 모든 채팅 목록 로드
    fun loadChats(email: String) {
        viewModelScope.launch {
            try {
                val chats = chatRepository.getChatsByUser(email)
                _chatList.value = chats
                Log.d("ChatBotViewModel", "Loaded chats: $chats")
            } catch (e: Exception) {
                Log.e("ChatBotViewModel", "Error loading chats: ${e.message}")
            }
        }
    }

    fun loadMessages(chatId: Int) {
        viewModelScope.launch {
            val messages = chatRepository.getMessagesByChatId(chatId)
            _messages.value = messages
        }
    }

    fun getMessagesForChat(chatId: Int): LiveData<List<Message>> {
        val messages = MutableLiveData<List<Message>>()
        viewModelScope.launch {
            val loadedMessages = chatRepository.getMessagesByChatId(chatId)
            messages.value = loadedMessages
        }
        return messages
    }


    // 새로운 채팅 생성
    fun createNewChat(email: String, onChatCreated: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val newChatId = chatRepository.createNewChat(email) // createNewChat은 채팅 ID를 반환
                onChatCreated(newChatId) // 콜백을 통해 채팅 ID를 전달
                loadChats(email) // 새로운 채팅 생성 후 목록 갱신
            } catch (e: Exception) {
                Log.e("ChatBotViewModel", "Error creating new chat: ${e.message}")
            }
        }
    }
    // 메시지 전송 처리
    fun sendMessage(email: String, message: String, chatId: Int) {
        // 사용자가 보낸 메시지 저장
        addMessageToChat(email, message, isUser = true, chatId)
        loadMessages(chatId)

        viewModelScope.launch {
            try {
                val chatBotClientDto = ChatBotClientDto(sender = email, message = message)
                val response = chatBotImpl.chatWithRasa(chatBotClientDto)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // 챗봇 응답 처리
                    if (responseBody != null && responseBody.data != null) {
                        responseBody.data.forEach { rasaDto ->

//                            rasaDto.text?.let { text ->
//                                addMessageToChat(email, text, isUser = false, chatId) // 챗봇 메시지 저장
//                                loadMessages(chatId)
//                            }

                            if (rasaDto.text != null) {
                                val text = rasaDto.text
                                addMessageToChat(email, text, isUser = false, chatId) // 챗봇 메시지 저장
                                loadMessages(chatId)
                            }

                            rasaDto.medicineList?.let { medicineList ->
                                medicineList.forEach { medicine ->
                                    val formattedMessage = buildString {
                                        append("제품명: ${medicine.itemName}\n")
                                        append("제품 분류: ${medicine.className}\n")
                                        append("성상: ${medicine.chart}")
                                    }
                                    addMessageToChat(email, formattedMessage, isUser = false, chatId)
                                    loadMessages(chatId)
                                }
                            }
                        }
                    } else {
                        addMessageToChat(email, "챗봇으로부터 응답이 없습니다.", isUser = false, chatId)
                        loadMessages(chatId)
                    }
                } else {
                    addMessageToChat(email, "오류 발생: ${response.code()} - ${response.message()}", isUser = false, chatId)
                    loadMessages(chatId)
                }
            } catch (e: Exception) {
                addMessageToChat(email, "예외 발생: ${e.message}", isUser = false, chatId)
                loadMessages(chatId)
            }
        }
    }

    // Firestore에 메시지를 저장하는 함수
    private fun addMessageToChat(email: String, message: String, isUser: Boolean, chatId: Int) {
        viewModelScope.launch {
            try {
                val newMessage = Message(
                    id = System.currentTimeMillis().toInt(),
                    content = message,
                    time = getCurrentTime(),
                    user = isUser
                )

                // Firestore 하위 컬렉션에 메시지 저장
                chatRepository.saveMessage(chatId, newMessage)
            } catch (e: Exception) {
                Log.e("ChatBotViewModel", "Error saving message: ${e.message}")
            }
        }
    }

    // 현재 시간을 포맷팅하는 함수
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
