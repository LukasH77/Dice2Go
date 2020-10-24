package com.example.dice.home

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dice.R

open class Die(
    var sidesAmount: Int,
    var initialSide: Int,
    var sides: List<Int>,
    var recentSides: MutableList<Int>,
    var visibility: Boolean,
    var uiRepresentation: ImageView
) {
    companion object {

        var time = 0

        fun setVisibility(dice: MutableList<Die>) {
            for (die in dice) {
                if (die.visibility) {
                    die.uiRepresentation.visibility = View.VISIBLE
                } else {
                    die.uiRepresentation.visibility = View.GONE
                    die.uiRepresentation.setImageResource(die.initialSide)
                }
            }
        }

        fun roll(dice: MutableList<Die>): Int {
            return if (time == 1) {
                println("roll, timer: $time")
                time--
                time
            } else {
                for (die in dice) {
                    var current = die.sides[(0 until die.sidesAmount).random()]
                    val currentList = die.recentSides
                    when (currentList.size) {
                        1 -> while (current == currentList.last()) {
                            current = die.sides[(0 until die.sidesAmount).random()]
                        }
                        else -> while (current == currentList.last()
                            || current == currentList[currentList.lastIndex - 1]
                        ) {
                            current = die.sides[(0 until die.sidesAmount).random()]
                        }
                    }
                    currentList.add(current)
                    die.uiRepresentation.setImageResource(current)
                }
                time--
                time
            }
        }

        fun removeDieMenu(dieMenu: ConstraintLayout) {
            dieMenu.visibility = View.GONE
        }

        fun resetBackground(dice: MutableList<Die>) {
            for (die in dice) {
                die.uiRepresentation.background = null
            }
        }
    }

    fun setupDieClicks(
        dice: MutableList<Die>,
        dieMenu: ConstraintLayout,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        replaceD8Button: ImageButton,
        removeButton: ImageButton,
        exitButton: ImageButton
    ) {
        val selectedIndex = dice.indexOf(this)
        this.uiRepresentation.setOnClickListener {
            resetBackground(dice)
            this.uiRepresentation.setBackgroundResource(R.color.primaryColor)
            dieMenu.visibility = View.VISIBLE
            addOrRemove(
                selectedIndex,
                dice,
                dieMenu,
                replaceD4Button,
                replaceD6Button,
                replaceD8Button,
                removeButton,
                exitButton
            )
        }
    }

    private fun addOrRemove(
        selectedIndex: Int,
        dice: MutableList<Die>,
        dieMenu: ConstraintLayout,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        replaceD8Button: ImageButton,
        removeButton: ImageButton,
        exitButton: ImageButton
    ) {
        println(selectedIndex)
        replaceD4Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D4(this.uiRepresentation))


            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d4__4)
            dice[selectedIndex].visibility = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
        }

        replaceD6Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D6(this.uiRepresentation))


            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d6__6)
            dice[selectedIndex].visibility = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
        }

        replaceD8Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D8(this.uiRepresentation))


            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d8__8)
            dice[selectedIndex].visibility = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
        }

        removeButton.setOnClickListener {
            resetBackground(dice)
            var lastVisibleIndex = 5
            for (i in dice.indices) {
                if (!dice[i].visibility) {
                    lastVisibleIndex = i - 1
                    break
                }
            }

//            println("lastVisibleIndex: $lastVisibleIndex")
//            println("selectedIndex: $selectedIndex")
            val steps = lastVisibleIndex - selectedIndex
//            println(dice)
            for (i in 0 until steps) {
//                val temp = dice[selectedIndex + i + 1]
//
//                dice.removeAt(selectedIndex + i)
//
//                when (temp) {
//                    is D4 -> {
//                        println("d4")
//                        dice.add(selectedIndex + i, D4(temp.uiRepresentation))
//                        dice[selectedIndex + i].visibility = temp.visibility
//                    }
//                    is D6 -> {
//                        println("d6")
//                        dice.add(selectedIndex + i, D6(temp.uiRepresentation))
//                        dice[selectedIndex + i].visibility = temp.visibility
//                    }
//                }
//                println("temp2: $temp2")
//
//                temp2.uiRepresentation = temp1.uiRepresentation
//                                println("${temp.recentSides}")
//                dice[selectedIndex+ i] = dice[selectedIndex + i + 1]

                //TODO This is basically cheating ... but it works!!!! Makes the subclasses a bit redundant..they're still used for replacements though, keep them in. It works. :D
                // Not the prettiest solution, gotta admit that. OR IS IT?!
                val replaced = dice[selectedIndex + i]
                val replacement = dice[selectedIndex + i + 1]
                replaced.uiRepresentation.setImageDrawable(replacement.uiRepresentation.drawable)
                replaced.sidesAmount = replacement.sidesAmount
                replaced.initialSide = replacement.initialSide
                replaced.sides = replacement.sides
                replaced.recentSides = replacement.recentSides
            }
//            println(dice)
            dieMenu.visibility = View.GONE
            dice[lastVisibleIndex].visibility = false
            setVisibility(dice)
        }

        exitButton.setOnClickListener {
            removeDieMenu(dieMenu)
            resetBackground(dice)
        }
    }
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
)

class D4(uiRepresentation: ImageView) : Die(
    4,
    R.drawable.d4__4,
    listOf(
        R.drawable.d4__1,
        R.drawable.d4__2,
        R.drawable.d4__3,
        R.drawable.d4__4
    ),
    mutableListOf(R.drawable.d4__4),
    false,
    uiRepresentation
)

class D8(uiRepresentation: ImageView) : Die(
    8,
    R.drawable.d8__8,
    listOf(
        R.drawable.d8__1,
        R.drawable.d8__2,
        R.drawable.d8__3,
        R.drawable.d8__4,
        R.drawable.d8__5,
        R.drawable.d8__6,
        R.drawable.d8__7,
        R.drawable.d8__8
    ),
    mutableListOf(R.drawable.d8__8),
    false,
    uiRepresentation
)


