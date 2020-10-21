package com.example.dice.home

import android.widget.ImageView
import com.example.dice.R

open class Die(
    val sidesAmount: Int,
    val initialSide: Int,
    val sides: List<Int>,
    val recentSides: MutableList<Int>,
    var visibility: Boolean,
    val uiRepresentation: ImageView
) {

}

class D6(uiRepresentation: ImageView) : Die(
    6,
    R.drawable.d6__6,
    listOf(
        R.drawable.d6__1,
        R.drawable.d6__2,
        R.drawable.d6__3,
        R.drawable.d6__4,
        R.drawable.d6__5,
        R.drawable.d6__6
    ),
    mutableListOf(R.drawable.d6__6),
    false,
    uiRepresentation
) {

}

