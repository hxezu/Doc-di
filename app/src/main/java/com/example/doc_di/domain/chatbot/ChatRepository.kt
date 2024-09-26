package com.example.doc_di.domain.chatbot

import com.example.doc_di.domain.model.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRepository {

    // 사용자 이메일을 키로 사용하는 채팅 기록 저장소
    private val chatDataStore: MutableMap<String, MutableList<Chat>> = mutableMapOf()

    // 특정 사용자의 채팅 기록 가져오기
    fun getChatsByUser(email: String): List<Chat> {
        return chatDataStore[email] ?: emptyList()
    }

    // 채팅 기록 저장
    fun saveChat(chat: Chat) {
        val email = chat.email
        if (chatDataStore.containsKey(email)) {
            chatDataStore[email]?.add(chat)
        } else {
            chatDataStore[email] = mutableListOf(chat)
        }
    }

    // 새로운 채팅 세션 생성 (새로운 대화)
    fun createNewChat(email: String): Chat {
        val newId = (chatDataStore[email]?.size ?: 0) + 1
        val newChat = Chat(
            id = newId,
            email = email,
            message = "새로운 대화를 시작합니다.",
            time = getCurrentTime(),
            isUser = false
        )
        saveChat(newChat)
        return newChat
    }

    // 현재 시간 문자열 반환
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}