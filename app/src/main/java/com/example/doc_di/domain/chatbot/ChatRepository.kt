package com.example.doc_di.domain.chatbot

import android.util.Log
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.domain.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Firestore에서 특정 사용자의 채팅 목록을 가져옴
    suspend fun getChatsByUser(email: String): List<Chat> {
        return try {
            val chatDocuments = firestore.collection("chats")
                .whereEqualTo("email", email)
                .get().await()

            // 로드된 채팅이 비어 있는 경우 처리
            if (chatDocuments.isEmpty) {
                Log.d("ChatRepository", "No chats found for user: $email")
            } else {
                Log.d("ChatRepository", "Found chats for user: $email")
            }

            chatDocuments.toObjects(Chat::class.java)
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching chats: ${e.message}")
            emptyList() // 오류 발생 시 빈 리스트 반환
        }
    }

    // Firestore에서 특정 채팅을 ID로 가져옴
    suspend fun getChatById(chatId: Int): Chat? {
        return try {
            val chatDocument = firestore.collection("chats")
                .document(chatId.toString())
                .get().await()

            chatDocument.toObject(Chat::class.java)
        } catch (e: Exception) {
            null // 오류 발생 시 null 반환
        }
    }

    // Firestore에서 특정 채팅의 메시지들을 가져옴
    suspend fun getMessagesByChatId(chatId: Int): List<Message> {
        return try {
            val messagesDocuments = firestore.collection("chats")
                .document(chatId.toString())
                .collection("messages")
                .orderBy("time")
                .get().await()

            messagesDocuments.toObjects(Message::class.java)
        } catch (e: Exception) {
            emptyList() // 오류 발생 시 빈 리스트 반환
        }
    }

    // Firestore에 새로운 채팅을 생성 (빈 messages 컬렉션도 포함)
    suspend fun createNewChat(email: String, createdAt: String): String {
        val newId = System.currentTimeMillis().toInt()
        val newChat = Chat(
            id = newId,
            email = email,
            createdAt = createdAt
        )

        return try {
            firestore.collection("chats")
                .document(newId.toString())
                .set(newChat)
                .await()

            // 챗봇의 첫 번째 메시지 추가
            val firstMessage = Message(
                id = System.currentTimeMillis().toInt(),
                content = "대화를 시작합니다",
                time = getCurrentTime(),
                user = false // 챗봇 메시지로 설정
            )

            // Firestore 하위 컬렉션에 첫 번째 메시지 저장
            firestore.collection("chats")
                .document(newId.toString())
                .collection("messages")
                .add(firstMessage)
                .await()

            newId.toString() // 새로 생성된 채팅 ID 반환
        } catch (e: FirebaseFirestoreException) {
            // Firestore 관련 에러 처리
            println("Firestore error: ${e.message}")
            throw e
        } catch (e: Exception) {
            // 기타 오류 처리
            println("Error: ${e.message}")
            throw e
        }
    }

    // 메시지를 Firestore 하위 컬렉션에 저장
    suspend fun saveMessage(chatId: Int, message: Message) {
        try {
            firestore.collection("chats")
                .document(chatId.toString())
                .collection("messages") // 하위 컬렉션 사용
                .add(message) // 각 메시지를 messages 하위 컬렉션에 추가
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteChat(chatId: Int) {
        try {
            firestore.collection("chats")
                .document(chatId.toString())
                .delete()
                .await() // Firestore 비동기 처리
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error deleting chat: ${e.message}")
            throw e
        }
    }


    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}