package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.domain.model.Message
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    chatBotViewModel: ChatBotViewModel,
    chatId: Int? = null
) {

    val userInfo by userViewModel.userInfo.observeAsState()
    val chat by chatBotViewModel.getChatById(userInfo?.email!!, chatId!!).observeAsState()
    val chatList by chatBotViewModel.chatList.observeAsState()

    var message by remember { mutableStateOf("") }

    val selectedChat = chatList?.firstOrNull { it.id == chatId }

    LaunchedEffect(chatId, userInfo) {
        userInfo?.email?.let { email ->
            chatBotViewModel.loadChats(email)
        }
    }

    LaunchedEffect(chatList) {
        userInfo?.email?.let { email ->
            chatBotViewModel.loadChats(email)
        }
    }


    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                navigationIcon = {
                    androidx.compose.material3.IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
            )
        },
        bottomBar = {
        BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
    },

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                ChatTitleRow(modifier = Modifier.padding(start = 20.dp, end = 20.dp))
                Box(
                    modifier = Modifier
                        .weight(1f) // 가용 공간을 채우기 위한 weight 사용
                        .consumeWindowInsets(paddingValues)
                        .imePadding() // 키보드 나올 때 맞춰서 올라가도록
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    ) {
                        val messages = selectedChat?.messages ?: emptyList()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 20.dp, top = 15.dp, end = 20.dp)
                        ) {
                            items(messages, key = { it.id }) { message ->
                                ChatRow(chat = message)
                            }
                            item {
                                Spacer(modifier = Modifier.height(90.dp))
                            }
                        }
                    }

                    // 3. CustomTextField도 imePadding으로 키보드 따라 움직임
                    CustomTextField(
                        text = message,
                        onValueChange = { message = it },
                        onSendClick = {
                            userInfo?.email?.let { email ->
                                if (message.isNotBlank()) {
                                    chatBotViewModel.sendMessage(email, message, selectedChat!!.id)
                                    message = ""
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                            .align(Alignment.BottomCenter) // 화면 하단에 위치
                            .consumeWindowInsets(paddingValues)
                            .imePadding() // 키보드가 나오면 위로 밀리게
                    )
                }
            }
        }
    }
}

@Composable
fun ChatRow(
    chat: Message
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalAlignment = if (chat.isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(if (chat.isUser) MainBlue else LightGray),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = chat.content,
                style = TextStyle(
                    color = if(chat.isUser) Color.White else Color.Black,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
                textAlign = TextAlign.Start
            )
        }
        Text(
            text = chat.time,
            style = TextStyle(
                color = Gray,
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
        )
    }

}

@Composable
fun CustomTextField(
    text: String,
    modifier: Modifier = Modifier,
    onSendClick: () -> Unit,
    onValueChange: (String) -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp,
        shape = RoundedCornerShape(164.dp),
        border = BorderStroke(1.dp, Gray400)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { onValueChange(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.type_message),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray
                        ),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = MainBlue,
                    textColor = Color.Black
                ),
                modifier = Modifier
                    .background(Color.Transparent)
                    .weight(2f),
                maxLines = 1,
                textStyle = TextStyle(fontSize = 14.sp)
            )
            IconButton(
                    onClick = onSendClick,
            modifier = Modifier
                .size(36.dp)
                .padding(end = 10.dp)
                .clip(CircleShape)
                .background(MainBlue)
            ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            }
        }
    }
}


@Composable
fun ChatTitleRow(
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "",
            modifier = Modifier.size(42.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.chatbot), style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.chatbot),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {}, modifier = Modifier
                .size(24.dp)
                .align(CenterVertically)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "", tint = MainBlue)
        }
    }

}
