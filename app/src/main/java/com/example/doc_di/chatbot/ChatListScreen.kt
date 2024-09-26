package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.UserViewModel
import com.example.doc_di.data.personList
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
    val nonNullChatList = chatList ?: emptyList()
    val userInfo by userViewModel.userInfo.observeAsState()

    LaunchedEffect(Unit) {
        if(userInfo != null){
            println("Fetching Chat for user: ${userViewModel.userInfo.value!!.email}")
        }else{
            println("User email is missing, cannot fetch Chat")
        }
    }

    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "DDoc-Di 와 대화하기",
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 60.dp, bottom = 30.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MainBlue,
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 90.dp)
                    ) {
//                        items(nonNullChatList, key = { it.id }) { chat ->
//                            ChatEachRow(chat = chat){
//                                navController.currentBackStackEntry?.savedStateHandle?.set("data",it)
//                                navController.navigate(Routes.chatScreen.route)
//                            }
//                        }
                    }
                }
            }
            IconButton(
                onClick = { navController.navigate(Routes.chatScreen.route) },
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(16.dp, bottom = 150.dp)
                    .border(2.dp, MainBlue, CircleShape)
                ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Chat",
                    tint = MainBlue
                )
            }

        }
    }
}


@Composable
fun ChatEachRow(
    chat: Chat,
    onClick:()->Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
                            text = chat.id.toString(), style = TextStyle(
                                color = Color.Black, fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Okay", style = TextStyle(
                                color = Gray, fontSize = 14.sp
                            )
                        )
                    }

                }
                Text(
                    text = "12:23 PM", style = TextStyle(
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
