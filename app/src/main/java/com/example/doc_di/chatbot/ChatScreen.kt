package com.example.doc_di.chatbot

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doc_di.R
import com.example.doc_di.domain.model.Message
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.etc.Routes
import com.example.doc_di.etc.throttleFirst
import com.example.doc_di.login.UserViewModel
import com.example.doc_di.login.rememberImeState
import com.example.doc_di.search.SearchViewModel
import com.example.doc_di.search.pillsearch.searchresult.pill_information.ReviewViewModel
import com.example.doc_di.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    btmBarViewModel: BtmBarViewModel,
    userViewModel: UserViewModel,
    chatBotViewModel: ChatBotViewModel,
    searchViewModel: SearchViewModel,
    reviewViewModel: ReviewViewModel,
    chatId: Int? = null
) {
    val userInfo by userViewModel.userInfo.observeAsState()
    val messages by chatBotViewModel.messages.observeAsState(emptyList())
    var message by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val imeState = rememberImeState()

    val context = LocalContext.current

    LaunchedEffect(chatId) {
        chatId?.let {
            chatBotViewModel.loadMessages(it) // 메시지 하위 컬렉션을 로드
            listState.animateScrollToItem(messages.size)
        }
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    LaunchedEffect(imeState) {
        if (imeState.value) {  // Keyboard is visible
                listState.animateScrollToItem(messages.size)
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
                        onClick = {{
                            navController.popBackStack()
                            val nothing = ""
                        }.throttleFirst()

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
//                .verticalScroll(scrollState)
//                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                ChatTitleRow(modifier = Modifier.padding(start = 20.dp, end = 20.dp)){
                        chatId?.let {
                            chatBotViewModel.deleteChat(userInfo?.email ?: "", it) // Firebase에서 대화 삭제
                            navController.popBackStack() // 이전 화면으로 돌아감
                        }
                }
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
                            .imePadding()
                            .consumeWindowInsets(paddingValues)
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 20.dp, top = 15.dp, end = 20.dp)
                                .consumeWindowInsets(paddingValues)
                                .imePadding()
                        ) {
                            items(messages, key = { it.id }) { message ->
                                ChatRow(chat = message,
                                    navController = navController,
                                    searchViewModel = searchViewModel,
                                    chatBotViewModel = chatBotViewModel,
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(90.dp))
                            }
                        }
                    }
                    CustomTextField(
                        text = message,
                        onValueChange = { message = it },
                        onSendClick = {
                                userInfo?.email?.let { email ->
                                    if (message.isNotBlank() && chatId != null) {
                                        Log.d("ChatScreen", "Send Available")
                                        chatBotViewModel.sendMessage(email, message, chatId, searchViewModel)
                                        message = ""
                                    }else{
                                        Log.d("ChatScreen", "Send Failed")
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
    chat: Message,
    navController: NavController,
    searchViewModel: SearchViewModel,
    chatBotViewModel: ChatBotViewModel,

) {
    val isMedicineInfoMessage = chat.content.contains("검색하고 있습니다...")
    val pillList = searchViewModel.pills.collectAsState().value
    val isLoading = searchViewModel.isLoading.collectAsState().value

    LaunchedEffect(navController.currentBackStackEntry) {
        searchViewModel.resetPillInfo()
        searchViewModel.resetPills()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),

        horizontalAlignment = if (chat.user) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (chat.user) MainBlue else LightGray)
                .padding(horizontal = 15.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = chat.content,
                style = TextStyle(
                    color = if(chat.user) Color.White else Color.Black,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Start
            )
        }
        val lastMessageTime = chat.time.let { time ->
            time.substring(11, 16) // "HH:mm" 형식으로 변환
        }

        Text(
            text =  lastMessageTime,
            style = TextStyle(
                color = Gray,
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
        )

        if(isMedicineInfoMessage){
            Button(
                onClick = {{
                    navController.navigate(Routes.chatbotSearchResult.route)
                }.throttleFirst()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainBlue,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            ) {
                Text("검색 결과 보러 가기")
            }
        }
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
                onClick = {
                    Log.d("ChatScreen", "Send button clicked")
                    onSendClick()
                          },
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
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
        Box(modifier = Modifier.align(CenterVertically)){
            IconButton(
                onClick = { {expanded = true }.throttleFirst()},
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "", tint = MainBlue)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)
                    .align(Alignment.TopEnd)

            ) {
                DropdownMenuItem(
                    onClick = { {
                        onDeleteClick() // 삭제 클릭 시 호출
                        expanded = false // 드롭다운 메뉴 닫기
                    }.throttleFirst()

                    }
                ) {
                    Text(text = "삭제")
                }
            }
        }

    }


}
