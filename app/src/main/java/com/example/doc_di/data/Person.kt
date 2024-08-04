package com.example.doc_di.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.doc_di.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: Int = 0,
    val name: String = "",
    @DrawableRes val icon: Int = R.drawable.icon
):Parcelable

val personList = listOf(
    Person(
        1,
        "사랑니 진통제 추천",
        R.drawable.icon
    ),
    Person(
        2,
        "빨간색 흰색 캡슐 알약",
        R.drawable.icon
    ),
    Person(
        3,
        "편의점 감기약",
        R.drawable.icon
    )
)