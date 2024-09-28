package com.example.doc_di.domain.chatbot

import com.example.doc_di.domain.model.Chat
import com.example.doc_di.domain.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ChatRepository {

    // 사용자 이메일을 키로 사용하는 채팅 기록 저장소
    private val chatDataStore: MutableMap<String, MutableList<Chat>> = mutableMapOf()

    // 특정 사용자의 채팅 기록 가져오기
    fun getChatsByUser(email: String): List<Chat> {
        return chatDataStore[email] ?: emptyList()
    }

    fun getChatById(email: String, chatId: Int): Chat? {
        return chatDataStore[email]?.firstOrNull { it.id == chatId }
    }

    // 채팅 기록 저장
    fun saveChat(email: String, chatId: Int, chat: Chat) {
        val chats = chatDataStore.getOrPut(email) { mutableListOf() }

        // Find the index of the existing chat with the same chatId
        val existingChatIndex = chats.indexOfFirst { it.id == chatId }

        if (existingChatIndex != -1) {
            // Update the existing chat
            chats[existingChatIndex] = chat
        } else {
            // Add as a new chat if it doesn't exist
            chats.add(chat)
        }
    }

    // 새로운 채팅 세션 생성 (새로운 대화)
    fun createNewChat(email: String): Chat {
        val newId = System.currentTimeMillis()
        val initialMessages = mutableListOf<Message>()

        val initialMessageId = chatDataStore[email]?.flatMap { it.messages }?.size?.plus(1) ?: 1 // Unique ID for the message

        initialMessages.add(
            Message(
                id = initialMessageId,  // Provide a unique ID for the new message
                content = "새로운 대화를 시작합니다.",
                time = getCurrentTime(),
                isUser = false
            )
        )

        val newChat = Chat(
            id = newId.toInt(),
            email = email,
            messages = initialMessages
        )
        saveChat(email,newChat.id, newChat)
        return newChat
    }

    // 현재 시간 문자열 반환
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}