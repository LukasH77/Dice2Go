package com.example.dice.home

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dice.R

open class Die(
    var sidesAmount: Int,
    var initialSide: Int,
    var sides: List<Int>,
    var recentSides: MutableList<Int>,
    var isVisible: Boolean,
    var isActive: Boolean,
    var uiRepresentation: ImageView
) {
    companion object {

        var time = 0

        fun setVisibility(dice: MutableList<Die>) {
            for (die in dice) {
                if (die.isVisible) {
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
                    if (!die.isActive) continue
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

        fun setupSelectionMenu(
            dice: MutableList<Die>,
            addButton: Button,
            selectButton: Button,
            dieMenu: ConstraintLayout,
            selectMenu: ConstraintLayout,
            addD4Button: ImageButton,
            addD6Button: ImageButton,
            addD8Button: ImageButton,
            exitSelectButton: ImageButton
        ) {
            handleButtons(dice, addButton, selectButton)
            var lastVisibleIndex = 0

            addD4Button.setOnClickListener {
                lastVisibleIndex = findLastVisibleIndex(dice)
                println(lastVisibleIndex)
                for (i in lastVisibleIndex until dice.lastIndex) {
                    val addedDie = dice[i + 1]
                    val modelDie = D4(addedDie.uiRepresentation)
                    addedDie.uiRepresentation.setImageResource(modelDie.initialSide)
                    addedDie.sidesAmount = modelDie.sidesAmount
                    addedDie.initialSide = modelDie.initialSide
                    addedDie.sides = modelDie.sides
                    addedDie.recentSides = modelDie.recentSides
                    addedDie.isActive = modelDie.isActive
                }
                dice[lastVisibleIndex + 1].isVisible = true
                setVisibility(dice)
                removeMenus(dieMenu, selectMenu)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d4"
            }

            addD6Button.setOnClickListener {
                lastVisibleIndex = findLastVisibleIndex(dice)
                println(lastVisibleIndex)
                for (i in lastVisibleIndex until dice.lastIndex) {
                    val addedDie = dice[i + 1]
                    val modelDie = D6(addedDie.uiRepresentation)
                    addedDie.uiRepresentation.setImageResource(modelDie.initialSide)
                    addedDie.sidesAmount = modelDie.sidesAmount
                    addedDie.initialSide = modelDie.initialSide
                    addedDie.sides = modelDie.sides
                    addedDie.recentSides = modelDie.recentSides
                    addedDie.isActive = modelDie.isActive
                }
                dice[lastVisibleIndex + 1].isVisible = true
                setVisibility(dice)
                removeMenus(dieMenu, selectMenu)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d6"
            }

            addD8Button.setOnClickListener {
                lastVisibleIndex = findLastVisibleIndex(dice)
                println(lastVisibleIndex)
                for (i in lastVisibleIndex until dice.lastIndex) {
                    val addedDie = dice[i + 1]
                    val modelDie = D8(addedDie.uiRepresentation)
                    addedDie.uiRepresentation.setImageResource(modelDie.initialSide)
                    addedDie.sidesAmount = modelDie.sidesAmount
                    addedDie.initialSide = modelDie.initialSide
                    addedDie.sides = modelDie.sides
                    addedDie.recentSides = modelDie.recentSides
                    addedDie.isActive = modelDie.isActive
                }
                dice[lastVisibleIndex + 1].isVisible = true
                setVisibility(dice)
                removeMenus(dieMenu, selectMenu)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d8"
            }

            exitSelectButton.setOnClickListener {
                removeMenus(dieMenu, selectMenu)
            }
        }

        fun removeMenus(dieMenu: ConstraintLayout, selectMenu: ConstraintLayout) {
            dieMenu.visibility = View.GONE
            selectMenu.visibility = View.GONE
        }

        fun resetBackground(dice: MutableList<Die>) {
            for (die in dice) {
                die.uiRepresentation.background = null
            }
        }

        fun handleButtons(dice: MutableList<Die>, addButton: Button, selectButton: Button) {
            println("lastVisibleIndex: ${findLastVisibleIndex(dice)}")
            when (findLastVisibleIndex(dice)) {
                5 -> {
                    selectButton.isEnabled = false
                    addButton.isEnabled = false
                }
                -1 -> {
                    selectButton.isEnabled = true
                    addButton.isEnabled = false
                }
                else -> {
                    selectButton.isEnabled = true
                    addButton.isEnabled = true
                }
            }
        }

        fun findLastVisibleIndex(dice: MutableList<Die>): Int {
            var lastVisibleIndex = 5
            for (i in dice.indices) {
                if (!dice[i].isVisible) {
                    lastVisibleIndex = i - 1
                    break
                }
            }
            return lastVisibleIndex
        }
    }

    fun setupDieClicks(
        dice: MutableList<Die>,
        dieMenu: ConstraintLayout,
        selectMenu: ConstraintLayout,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        replaceD8Button: ImageButton,
        removeButton: ImageButton,
        holdButton: ImageButton,
        exitButton: ImageButton,
        addButton: Button,
        selectButton: Button
    ) {
        val selectedIndex = dice.indexOf(this)
        this.uiRepresentation.setOnClickListener {

            resetBackground(dice)
            this.uiRepresentation.setBackgroundResource(R.color.primaryColor)
            dieMenu.visibility = View.VISIBLE
            if (this.isActive) holdButton.setImageResource(android.R.drawable.ic_media_pause) else holdButton.setImageResource(
                android.R.drawable.ic_media_play
            )
            addOrRemove(
                selectedIndex,
                dice,
                dieMenu,
                selectMenu,
                replaceD4Button,
                replaceD6Button,
                replaceD8Button,
                removeButton,
                holdButton,
                exitButton,
                addButton,
                selectButton
            )
        }
    }

    private fun addOrRemove(
        selectedIndex: Int,
        dice: MutableList<Die>,
        dieMenu: ConstraintLayout,
        selectMenu: ConstraintLayout,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        replaceD8Button: ImageButton,
        removeButton: ImageButton,
        holdButton: ImageButton,
        exitButton: ImageButton,
        addButton: Button,
        selectButton: Button
    ) {
        println(selectedIndex)
        replaceD4Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D4(this.uiRepresentation))
            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d4__4)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
        }

        replaceD6Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D6(this.uiRepresentation))


            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d6__6)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
        }

        replaceD8Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D8(this.uiRepresentation))


            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d8__8)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
        }

        removeButton.setOnClickListener {
            resetBackground(dice)
            val lastVisibleIndex = findLastVisibleIndex(dice)

//            println("lastVisibleIndex: $lastVisibleIndex")
//            println("selectedIndex: $selectedIndex")
            val steps = 5 - selectedIndex
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
                replaced.isActive = replacement.isActive
            }
            when (addButton.text) {
                "Add d4" -> dice[5] = D4(dice[5].uiRepresentation)
                "Add d6" -> dice[5] = D6(dice[5].uiRepresentation)
                "Add d8" -> dice[5] = D8(dice[5].uiRepresentation)
            }
//            println(dice)
            dieMenu.visibility = View.GONE
            dice[lastVisibleIndex].isVisible = false
            setVisibility(dice)
            handleButtons(dice, addButton, selectButton)
        }

        holdButton.setOnClickListener {
            if (this.isActive) {
                holdButton.setImageResource(android.R.drawable.ic_media_pause)
                this.isActive = false
                this.uiRepresentation.setImageResource(android.R.drawable.ic_lock_idle_lock)
                holdButton.setImageResource(android.R.drawable.ic_media_play)
                resetBackground(dice)
                removeMenus(dieMenu, selectMenu)
            } else {
                holdButton.setImageResource(android.R.drawable.ic_media_play)
                this.isActive = true
                this.uiRepresentation.setImageResource(this.recentSides.last())
                holdButton.setImageResource(android.R.drawable.ic_media_pause)
                resetBackground(dice)
                removeMenus(dieMenu, selectMenu)
            }
        }

        exitButton.setOnClickListener {
            removeMenus(dieMenu, selectMenu)
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
    true,
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
    true,
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
    true,
    uiRepresentation
)


