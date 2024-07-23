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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.R
import com.example.doc_di.data.Chat
import com.example.doc_di.data.Person
import com.example.doc_di.data.chatList
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.BtmBarViewModel
import com.example.doc_di.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(navController: NavController, btmBarViewModel: BtmBarViewModel) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController, btmBarViewModel = btmBarViewModel) }) {
            paddingValues ->
        var message by remember { mutableStateOf("") }
        val data = navController.previousBackStackEntry?.savedStateHandle?.get<Person>("data") ?: Person()

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
                UserNameRow(
                    person = data,
                    modifier = Modifier.padding(top = 60.dp, start = 20.dp, end = 20.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 25.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 30.dp, topEnd = 30.dp
                            )
                        )
                        .background(Color.White)
                ) {
                    LazyColumn(modifier = Modifier.padding(start = 15.dp, top = 25.dp, end = 15.dp)){
                        items(chatList,key={it.id}){
                            ChatRow(chat = it)
                        }
                    }

                }
            }
            CustomTextField(
                text = message, onValueChange = { message = it },
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
        horizontalAlignment = if (chat.direction) Alignment.Start else Alignment.End
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(
                    if (chat.direction) LightGray else MainBlue
                ),
            contentAlignment = Center
        ) {
            Text(
                text = chat.message, style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 15.dp),
                textAlign = TextAlign.End
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
    onValueChange: (String) -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp,
        shape = RoundedCornerShape(164.dp),
        border = BorderStroke(1.dp, Gray400)
    ) {
        TextField(
            value = text, onValueChange = { onValueChange(it) },
            placeholder = {
                androidx.compose.material.Text(
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
                focusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = { CommonIconButton(imageVector = Icons.Default.Add) }

        )
    }

}

@Composable
fun CommonIconButton(
    imageVector: ImageVector
) {

    Box(
        modifier = Modifier
            .size(33.dp)
            .clip(CircleShape)
            .background(MainBlue), contentAlignment = Center
    ) {
        Icon(
            imageVector = imageVector, contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(15.dp)
        )
    }

}


@Composable
fun UserNameRow(
    modifier: Modifier = Modifier,
    person: Person
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                painter = painterResource(id = person.icon),
                contentDescription = "",
                modifier = Modifier.size(42.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = person.name, style = TextStyle(
                        color = MainBlue,
                        fontSize = 16.sp
                    )
                )
                androidx.compose.material.Text(
                    text = stringResource(R.string.chatbot), style = TextStyle(
                        color = MainBlue,
                        fontSize = 14.sp
                    )
                )
            }
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

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview(){
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    ChatScreen(navController = navController, btmBarViewModel = btmBarViewModel)
}

@Preview(showBackground = true, name = "CustomTextField Preview")
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(text = "", onValueChange = {})
}