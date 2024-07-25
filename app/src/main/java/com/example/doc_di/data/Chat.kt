package com.example.doc_di.data

data class Chat(
    val id:Int,
    val message:String,
    val time:String,
    val direction:Boolean
)

val chatList = listOf(
    Chat(
        1,
        "Hi",
        "12:15 PM",
        true
    ),
    Chat(
        2,
        "Hello",
        "12:17 PM",
        true
    ),
    Chat(
        3,
        "consectetur adipisicing elit",
        "12:19 PM",
        false
    ),
    Chat(
        4,
        "Good",
        "12:20 PM",
        false
    ),
    Chat(
        5,
        "Lorem ipsum dolor sit amet",
        "12:21 PM",
        true
    ),
    Chat(
        6,
        "Hi",
        "12:15 PM",
        false
    ),
    Chat(
        7,
        "Hello",
        "12:17 PM",
        true
    ),
    Chat(
        8,
        "Good",
        "12:19 PM",
        false
    ),
    Chat(
        9,
        "Nice",
        "12:20 PM",
        false
    ),
    Chat(
        10,
        "Good Morning",
        "12:21 PM",
        true
    ),
)
