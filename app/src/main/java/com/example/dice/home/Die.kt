package com.example.dice.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dice.R

open class Die(
    val sidesAmount: Int,
    val initialSide: Int,
    val sides: List<Int>,
    val recentSides: MutableList<Int>,
    var visibility: Boolean,
    val uiRepresentation: ImageView
) {
    companion object {

        var time = 0

        fun setVisibility(dice: List<Die>) {
            for (die in dice) {
                if (die.visibility) {
                    die.uiRepresentation.visibility = View.VISIBLE
                } else {
                    die.uiRepresentation.visibility = View.GONE
                    die.uiRepresentation.setImageResource(die.initialSide)
                }
            }
        }

        fun setImg(dice: List<Die>): Int {
            return if (time < 1) {
                for (die in dice) {
                    setVisibility(dice)
                }
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

        fun resetBackground(dice: List<Die>) {
            for (die in dice) {
                die.uiRepresentation.background = null
            }
        }
    }

    fun setupDieClicks(
        dice: List<Die>,
        dieMenu: ConstraintLayout,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        removeButton: ImageButton,
        exitButton: ImageButton
    ) {
        this.uiRepresentation.setOnClickListener {
            resetBackground(dice)
            this.uiRepresentation.setBackgroundResource(R.color.primaryColor)
            dieMenu.visibility = View.VISIBLE
            addOrRemove(dice, dieMenu, replaceD4Button, replaceD6Button, removeButton, exitButton)
        }
    }

    private fun addOrRemove(
        dice: List<Die>,
        dieMenu: ConstraintLayout,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        removeButton: ImageButton,
        exitButton: ImageButton
    ) {
        replaceD4Button.setOnClickListener {
            resetBackground(dice)
            this.uiRepresentation.setImageResource(this.initialSide)
            dieMenu.visibility = View.GONE
        }
        replaceD6Button.setOnClickListener {
            resetBackground(dice)
            this.uiRepresentation.setImageResource(this.initialSide)
            dieMenu.visibility = View.GONE
        }
        removeButton.setOnClickListener {
            resetBackground(dice)
            val currentIndex = dice.indexOf(this)
            var lastVisibleIndex = 5
            for (i in dice.indices) {
                if (!dice[i].visibility) {
                    lastVisibleIndex = i - 1
                    break
                }
            }
            val steps = lastVisibleIndex - currentIndex
            for (i in 0 until steps) {
                dice[currentIndex + i].uiRepresentation.setImageDrawable(dice[currentIndex + i + 1].uiRepresentation.drawable)
            }
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


