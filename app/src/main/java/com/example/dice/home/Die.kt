package com.example.dice.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginRight
import com.example.dice.R
import kotlinx.android.synthetic.main.fragment_home.*

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
            val lastVisibleIndex = findLastVisibleIndex(dice)

            for (die in dice) {
//                val param = die.uiRepresentation.layoutParams as ViewGroup.MarginLayoutParams

                if (die.isVisible) {
                    die.uiRepresentation.visibility = View.VISIBLE

//                    if (dice.indexOf(die) % 2 == 0 && dice.indexOf(die) == lastVisibleIndex) {
//                        param.setMargins(0, 0, 0, 0)
//                        die.uiRepresentation.layoutParams = param
//                    } else {
//                        if (dice.indexOf(die) % 2 == 0) {
//                            param.rightMargin = 8
//                            die.uiRepresentation.layoutParams = param
//                        } else {
//                            param.leftMargin = 8
//                            die.uiRepresentation.layoutParams = param
//                        }
//                    }

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
            hintText: TextView,
            totalText: TextView,
            dieMenu: ConstraintLayout,
            selectMenu: ConstraintLayout,
            addD4Button: ImageButton,
            addD6Button: ImageButton,
            addD8Button: ImageButton,
            addD10Button: ImageButton,
            addD12Button: ImageButton,
            addD20Button: ImageButton,
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
                removeMenus(dice, dieMenu, selectMenu, hintText)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d4"
                totalText.text = "Total: "
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
                removeMenus(dice, dieMenu, selectMenu, hintText)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d6"
                totalText.text = "Total: "
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
                removeMenus(dice, dieMenu, selectMenu, hintText)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d8"
                totalText.text = "Total: "
            }

            addD10Button.setOnClickListener {
                lastVisibleIndex = findLastVisibleIndex(dice)
                println(lastVisibleIndex)
                for (i in lastVisibleIndex until dice.lastIndex) {
                    val addedDie = dice[i + 1]
                    val modelDie = D10(addedDie.uiRepresentation)
                    addedDie.uiRepresentation.setImageResource(modelDie.initialSide)
                    addedDie.sidesAmount = modelDie.sidesAmount
                    addedDie.initialSide = modelDie.initialSide
                    addedDie.sides = modelDie.sides
                    addedDie.recentSides = modelDie.recentSides
                    addedDie.isActive = modelDie.isActive
                }
                dice[lastVisibleIndex + 1].isVisible = true
                setVisibility(dice)
                removeMenus(dice, dieMenu, selectMenu, hintText)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d10"
                totalText.text = "Total: "
            }

            addD12Button.setOnClickListener {
                lastVisibleIndex = findLastVisibleIndex(dice)
                println(lastVisibleIndex)
                for (i in lastVisibleIndex until dice.lastIndex) {
                    val addedDie = dice[i + 1]
                    val modelDie = D12(addedDie.uiRepresentation)
                    addedDie.uiRepresentation.setImageResource(modelDie.initialSide)
                    addedDie.sidesAmount = modelDie.sidesAmount
                    addedDie.initialSide = modelDie.initialSide
                    addedDie.sides = modelDie.sides
                    addedDie.recentSides = modelDie.recentSides
                    addedDie.isActive = modelDie.isActive
                }
                dice[lastVisibleIndex + 1].isVisible = true
                setVisibility(dice)
                removeMenus(dice, dieMenu, selectMenu, hintText)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d12"
                totalText.text = "Total: "
            }

            addD20Button.setOnClickListener {
                lastVisibleIndex = findLastVisibleIndex(dice)
                println(lastVisibleIndex)
                for (i in lastVisibleIndex until dice.lastIndex) {
                    val addedDie = dice[i + 1]
                    val modelDie = D20(addedDie.uiRepresentation)
                    addedDie.uiRepresentation.setImageResource(modelDie.initialSide)
                    addedDie.sidesAmount = modelDie.sidesAmount
                    addedDie.initialSide = modelDie.initialSide
                    addedDie.sides = modelDie.sides
                    addedDie.recentSides = modelDie.recentSides
                    addedDie.isActive = modelDie.isActive
                }
                dice[lastVisibleIndex + 1].isVisible = true
                setVisibility(dice)
                removeMenus(dice, dieMenu, selectMenu, hintText)
                handleButtons(dice, addButton, selectButton)
                addButton.text = "Add d20"
                totalText.text = "Total: "
            }

            exitSelectButton.setOnClickListener {
                selectMenu.visibility = View.GONE
                hintText.visibility = View.GONE
//                removeMenus(dice, dieMenu, selectMenu, hintText)
            }
        }

        fun removeMenus(
            dice: MutableList<Die>,
            dieMenu: ConstraintLayout,
            selectMenu: ConstraintLayout,
            hintText: TextView
        ) {
            if (!dice[0].isVisible) return
            dieMenu.visibility = View.GONE
            selectMenu.visibility = View.GONE
            hintText.visibility = View.GONE
        }

        fun resetBackground(dice: MutableList<Die>) {
            for (die in dice) {
                die.uiRepresentation.background = null
            }
        }

        fun handleButtons(dice: MutableList<Die>, addButton: Button, selectButton: Button) {
            println("lastVisibleIndex hb: ${findLastVisibleIndex(dice)}")
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
        hintText: TextView,
        totalText: TextView,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        replaceD8Button: ImageButton,
        replaceD10Button: ImageButton,
        replaceD12Button: ImageButton,
        replaceD20Button: ImageButton,
        removeButton: ImageButton,
//        holdButton: ImageButton,
        exitButton: ImageButton,
        addButton: Button,
        selectButton: Button
    ) {
        println("setting up die clicks")
        val selectedIndex = dice.indexOf(this)
        this.uiRepresentation.setOnClickListener {
            resetBackground(dice)
            this.uiRepresentation.setBackgroundResource(R.color.primaryColor)
            dieMenu.visibility = View.VISIBLE
//            if (this.isActive) holdButton.setImageResource(android.R.drawable.ic_media_pause) else holdButton.setImageResource(
//                android.R.drawable.ic_media_play
//            )
            addOrRemove(
                selectedIndex,
                dice,
                dieMenu,
                selectMenu,
                hintText,
                totalText,
                replaceD4Button,
                replaceD6Button,
                replaceD8Button,
                replaceD10Button,
                replaceD12Button,
                replaceD20Button,
                removeButton,
//                holdButton,
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
        hintText: TextView,
        totalText: TextView,
        replaceD4Button: ImageButton,
        replaceD6Button: ImageButton,
        replaceD8Button: ImageButton,
        replaceD10Button: ImageButton,
        replaceD12Button: ImageButton,
        replaceD20Button: ImageButton,
        removeButton: ImageButton,
//        holdButton: ImageButton,
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
            totalText.text = "Total: "
        }

        replaceD6Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D6(this.uiRepresentation))
            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d6__6)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
            totalText.text = "Total: "
        }

        replaceD8Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D8(this.uiRepresentation))
            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d8__8)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
            totalText.text = "Total: "
        }

        replaceD10Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D10(this.uiRepresentation))
            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d10__10)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
            totalText.text = "Total: "
        }

        replaceD12Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D12(this.uiRepresentation))
            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d12__12)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
            totalText.text = "Total: "
        }

        replaceD20Button.setOnClickListener {
            dice.removeAt(selectedIndex)
            dice.add(selectedIndex, D20(this.uiRepresentation))
            resetBackground(dice)
            dice[selectedIndex].uiRepresentation.setImageResource(R.drawable.d20__20)
            dice[selectedIndex].isVisible = true
            setVisibility(dice)
            dieMenu.visibility = View.GONE
            totalText.text = "Total: "
        }

        removeButton.setOnClickListener {
            resetBackground(dice)
            val lastVisibleIndex = findLastVisibleIndex(dice)

            val steps = 5 - selectedIndex
            for (i in 0 until steps) {
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
            dieMenu.visibility = View.GONE
            dice[lastVisibleIndex].isVisible = false
            setVisibility(dice)
            if (!dice[0].isVisible) {
                selectMenu.visibility = View.VISIBLE
                hintText.visibility = View.VISIBLE
                hintText.visibility = View.VISIBLE
                println("fault")
                addButton.text = "Add "
            }
            handleButtons(dice, addButton, selectButton)
            totalText.text = "Total: "
        }

//        holdButton.setOnClickListener {
//            if (this.isActive) {
//                holdButton.setImageResource(android.R.drawable.ic_media_pause)
//                this.isActive = false
//                this.uiRepresentation.setImageResource(android.R.drawable.ic_lock_idle_lock)
//                holdButton.setImageResource(android.R.drawable.ic_media_play)
//                resetBackground(dice)
//                removeMenus(dieMenu, selectMenu)
//            } else {
//                holdButton.setImageResource(android.R.drawable.ic_media_play)
//                this.isActive = true
//                this.uiRepresentation.setImageResource(this.recentSides.last())
//                println(this.recentSides.last())
//                println(this.recentSides)
//                holdButton.setImageResource(android.R.drawable.ic_media_pause)
//                resetBackground(dice)
//                removeMenus(dieMenu, selectMenu)
//            }
//        }

        exitButton.setOnClickListener {
            removeMenus(dice, dieMenu, selectMenu, hintText)
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

class D10(uiRepresentation: ImageView) : Die(
    10,
    R.drawable.d10__10,
    listOf(
        R.drawable.d10__1,
        R.drawable.d10__2,
        R.drawable.d10__3,
        R.drawable.d10__4,
        R.drawable.d10__5,
        R.drawable.d10__6,
        R.drawable.d10__7,
        R.drawable.d10__8,
        R.drawable.d10__9,
        R.drawable.d10__10
    ),
    mutableListOf(R.drawable.d10__10),
    false,
    true,
    uiRepresentation
)

class D12(uiRepresentation: ImageView) : Die(
    12,
    R.drawable.d12__12,
    listOf(
        R.drawable.d12__1,
        R.drawable.d12__2,
        R.drawable.d12__3,
        R.drawable.d12__4,
        R.drawable.d12__5,
        R.drawable.d12__6,
        R.drawable.d12__7,
        R.drawable.d12__8,
        R.drawable.d12__9,
        R.drawable.d12__10,
        R.drawable.d12__11,
        R.drawable.d12__12
    ),
    mutableListOf(R.drawable.d12__12),
    false,
    true,
    uiRepresentation
)

class D20(uiRepresentation: ImageView) : Die(
    20,
    R.drawable.d20__20,
    listOf(
        R.drawable.d20__1,
        R.drawable.d20__2,
        R.drawable.d20__3,
        R.drawable.d20__4,
        R.drawable.d20__5,
        R.drawable.d20__6,
        R.drawable.d20__7,
        R.drawable.d20__8,
        R.drawable.d20__9,
        R.drawable.d20__10,
        R.drawable.d20__11,
        R.drawable.d20__12,
        R.drawable.d20__13,
        R.drawable.d20__14,
        R.drawable.d20__15,
        R.drawable.d20__16,
        R.drawable.d20__17,
        R.drawable.d20__18,
        R.drawable.d20__19,
        R.drawable.d20__20
    ),
    mutableListOf(R.drawable.d20__20),
    false,
    true,
    uiRepresentation
)

