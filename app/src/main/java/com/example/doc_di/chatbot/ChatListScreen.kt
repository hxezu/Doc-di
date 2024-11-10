package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.FabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.domain.model.Message
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.isNetworkAvailable
import com.example.doc_di.ui.theme.Line
import com.example.doc_di.ui.theme.MainBlue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    chatBotViewModel: ChatBotViewModel
) {
    val context = LocalContext.current

    val chatList by chatBotViewModel.chatList.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()

    LaunchedEffect(Unit) {
        if (userInfo != null) {
            userInfo?.email?.let { email ->
                Log.d("ChatListScreen", "Fetching chats for user: $email")
                chatBotViewModel.loadChats(email)
            }
        } else {
            Log.d("ChatListScreen", "User email is missing, cannot fetch chats")
        }
    }

    Scaffold(
        backgroundColor = Color.Transparent,
        bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(isNetworkAvailable(context)){
                        userInfo?.email?.let { email ->
                            chatBotViewModel.createNewChat(email) { newChatId ->
                                navController.navigate("chat_screen/$newChatId") // 생성된 채팅 ID를 사용해 채팅 화면으로 이동
                            }
                        }
                    }else{
                        Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }

                          },
                backgroundColor = MainBlue,
                modifier = Modifier.padding(bottom = 20.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Chat",
                    tint = White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Transparent)
        ) {
            Text(
                text = "DDoc-Di 와 대화하기",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(start = 20.dp, top = 100.dp, bottom = 30.dp)
                    .background(Color.Transparent)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                if (chatList.isNullOrEmpty()) {
                    // chatList가 null이거나 비어 있을 때 보여줄 메시지
                    Text(
                        text = "DDoc-Di 와의 대화 기록이 없습니다.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                } else {
                    val sortedChatList = chatList!!.map { chat ->
                        val messages by chatBotViewModel.getMessagesForChat(chat.id).observeAsState(emptyList())
                        val lastMessage = messages.lastOrNull()
                        Pair(chat, lastMessage)
                    }
                        .filter { it.second != null } // 마지막 메시지가 있는 채팅만 필터링
                        .sortedByDescending { it.second!!.time } // 마지막 메시지 시간 기준으로 내림차순 정렬

                    LazyColumn(modifier = Modifier.padding(bottom = 90.dp)
                    ) {
                        items(sortedChatList) { (chat, lastMessage) ->
                            val chatName = "DDoc-Di 와의 대화"

                            ChatEachRow(
                                chat = chat,
                                lastMessage = lastMessage,
                                chatName = chatName
                            ) {
                                navController.navigate("chat_screen/${chat.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChatEachRow(
    chat: Chat,
    lastMessage: Message?,
    chatName: String,
    onClick:()->Unit
) {
    val lastMessageContent = lastMessage?.content ?: "No messages yet"
    val lastMessageTime = lastMessage?.time?.let { time ->
        // 'HH:mm:ss' 형식에서 시와 분만 추출
        time.substring(11, 16) // "HH:mm" 형식으로 변환
    } ?: "No messages yet"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .noRippleEffect { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (modifier = Modifier.weight(1f)){
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text =  chatName , style = TextStyle(
                                color = Color.Black, fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = lastMessageContent,
                            style = TextStyle(
                                color = Gray, fontSize = 14.sp
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
                Text(
                    text = lastMessageTime,
                    style = TextStyle(
                        color = Gray, fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Line)
        }
    }

}


fun Modifier.noRippleEffect(onClick: () -> Unit) = composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        onClick()
    }
}
