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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doc_di.data.Person
import com.example.doc_di.data.personList
import com.example.doc_di.etc.BottomNavigationBar
import com.example.doc_di.etc.Routes
import com.example.doc_di.home.BtmBarViewModel
import com.example.doc_di.ui.theme.Line
import com.example.doc_di.ui.theme.MainBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavController, btmBarViewModel: BtmBarViewModel) {
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
                        items(personList, key = { it.id }) {
                            UserEachRow(person = it){
                                navController.currentBackStackEntry?.savedStateHandle?.set("data",it)
                                navController.navigate(Routes.chatScreen.route)
                            }
                        }
                    }
                }
            }
            IconButton(
                onClick = { /* Handle Add Chat Click */ },
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
fun UserEachRow(
    person: Person,
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
                        painter = painterResource(id = person.icon),
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                    ) {
                        Text(
                            text = person.name, style = TextStyle(
                                color = Color.Black, fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        androidx.compose.material.Text(
                            text = "Okay", style = TextStyle(
                                color = Gray, fontSize = 14.sp
                            )
                        )
                    }

                }
                androidx.compose.material.Text(
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

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview(){
    val navController = rememberNavController()
    val btmBarViewModel: BtmBarViewModel = viewModel()
    ChatListScreen(navController = navController, btmBarViewModel = btmBarViewModel)
}