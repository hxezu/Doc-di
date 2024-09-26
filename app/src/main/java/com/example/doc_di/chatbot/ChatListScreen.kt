package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.ui.theme.Line
import com.example.doc_di.ui.theme.MainBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    chatBotViewModel: ChatBotViewModel
) {
    val chatList by chatBotViewModel.chatList.observeAsState()
    val userInfo by userViewModel.userInfo.observeAsState()

    LaunchedEffect(Unit) {
        if(userInfo != null){
            userInfo?.email?.let { email ->
                chatBotViewModel.loadChats(email)
            }
            Log.d("ChatListScreen", "Fetching Chat for user: ${userViewModel.userInfo.value!!.email}")
        }else{
            println("User email is missing, cannot fetch Chat")
        }
    }

    Scaffold(
        backgroundColor = Color.Transparent,
        bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    userInfo?.email?.let { email ->
                        val newChatId = chatBotViewModel.createNewChat(email)
                        navController.navigate("chat_screen/$newChatId") // 새로운 채팅 상세 화면으로 이동
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
                    // chatList가 비어 있지 않을 때 LazyColumn을 사용하여 리스트를 표시
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 90.dp)
                    ) {
                        items(chatList!!, key = { it.id }) { chat ->
                            ChatEachRow(chat = chat) {
                                navController.navigate("chat_screen/${chat.id}") // 특정 채팅 상세 화면으로 이동
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
    onClick:()->Unit
) {
    val lastMessage = chat.messages.lastOrNull()
    val lastMessageContent = lastMessage?.content?: "No messages yet"
    val lastMessageTime = lastMessage?.time?: "No messages yet"

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
                Row {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                    ) {
                        Text(
                            text = chat.id.toString() + "번째 대화", style = TextStyle(
                                color = Color.Black, fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = lastMessageContent,
                            style = TextStyle(
                                color = Gray, fontSize = 14.sp
                            )
                        )
                    }

                }
                Text(
                    text = lastMessageTime,
                    style = TextStyle(
                        color = Gray, fontSize = 12.sp
                    )
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
