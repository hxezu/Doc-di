package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment.Companion.BottomCenter
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
import com.example.doc_di.UserViewModel
import com.example.doc_di.domain.model.Chat
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.observeAsState
import com.example.doc_di.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    chatBotViewModel: ChatBotViewModel
) {
    val chatList by chatBotViewModel.chatList.observeAsState()
    val nonNullChatList = chatList ?: emptyList()
    val userInfo by userViewModel.userInfo.observeAsState()


    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if(userInfo != null){
            println("Create Chat for user: ${userViewModel.userInfo.value!!.email}")
        }else{
            println("User email is missing, cannot create Chat")
        }
    }


    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel)
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
            ) {
                ChatTitleRow(
                    modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = 30.dp, topEnd = 30.dp
                            )
                        )
                        .background(Color.White)
                ) {
                    LazyColumn(modifier = Modifier.padding(start = 20.dp, top = 15.dp, end = 20.dp)
                    ){
                        items(nonNullChatList, key={it.id}) { chat ->
                            ChatRow(chat = chat)
                        }
                    }

                }
            }
            CustomTextField(
                text = message,
                onValueChange = { message = it },
                onSendClick ={
                    if(message.isNotBlank()){
                        chatBotViewModel.sendMessage(message, userInfo!!.email)
                        message = ""
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .align(BottomCenter)
            )

        }


    }
}

@Composable
fun ChatRow(
    chat: Chat
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (chat.isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(
                    if (chat.isUser) MainBlue else LightGray
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = chat.message, style = TextStyle(
                    color = if(chat.isUser) Color.White else Color.Black,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
                textAlign = TextAlign.Start
            )
        }
        Text(
            text = chat.time, style = TextStyle(
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
                .padding(8.dp),
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
                            color = Color.Black
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
                modifier = Modifier.weight(2f),
                maxLines = 3
            )
            IconButton(
                    onClick = onSendClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MainBlue)
            ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White
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
                    color = MainBlue,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.chatbot),
                style = TextStyle(
                    color = MainBlue,
                    fontSize = 14.sp
                )
            )
        }
        IconButton(
            onClick = {}, modifier = Modifier
                .size(24.dp)
                .align(CenterVertically)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "", tint = MainBlue)
        }
    }

}
