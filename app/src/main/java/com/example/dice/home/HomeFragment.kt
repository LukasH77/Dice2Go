package com.example.dice.home

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dice.R
import com.example.dice.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var timer: Timer

    private lateinit var dice: List<Die>

    private lateinit var dieMenu: ConstraintLayout
    private lateinit var selectMenu: ConstraintLayout
    private lateinit var hintText: TextView

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val holdButton = binding.ibHold
//        val modifierText = binding.modifier
//        val modifierUpButton = binding.modifierUp
//        val modifierDownButton = binding.modifierDown
//        var modifier = 0

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

        val preferencesKey = getString(R.string.preferences_key)

        val animationPreferenceKey = getString(R.string.animation_preference)
        val soundPreferenceKey = getString(R.string.sound_preference)
        val vibrationPreferenceKey = getString(R.string.vibration_preference)

        val preferences = activity?.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)

        val isAnimationOn = preferences?.getBoolean(animationPreferenceKey, true)
        val isSoundOn = preferences?.getBoolean(soundPreferenceKey, true)
        val isVibrationOn =
            preferences?.getBoolean(vibrationPreferenceKey, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

        dice = mutableListOf(
            D6(binding.ivDie1),
            D4(binding.ivDie2),
            D8(binding.ivDie3),
            D10(binding.ivDie4),
            D12(binding.ivDie5),
            D20(binding.ivDie6)
        )
        dieMenu = binding.clPopup
        selectMenu = binding.clSelectionPopup
        hintText = binding.tvHint

        val replaceD4Button = binding.ibReplaceD4
        val replaceD6Button = binding.ibReplaceD6
        val replaceD8Button = binding.ibReplaceD8
        val replaceD10Button = binding.ibReplaceD10
        val replaceD12Button = binding.ibReplaceD12
        val replaceD20Button = binding.ibReplaceD20
        val removeButton = binding.ibRemove

        val exitButton = binding.ibExit

        val addD4Button = binding.ibAddD4
        val addD6Button = binding.ibAddD6
        val addD8Button = binding.ibAddD8
        val addD10Button = binding.ibAddD10
        val addD12Button = binding.ibAddD12
        val addD20Button = binding.ibAddD20
        val exitSelectButton = binding.ibExitSelect

        val clearButton = binding.bClear
        val rollButton = binding.bRoll
        val selectButton = binding.bSelectDie
        val addButton = binding.bAdd
        val totalText = binding.tvTotal

        var total = 0

        val rollingBuzzer = activity?.getSystemService<Vibrator>()

        var rollingSound = MediaPlayer.create(this.context, R.raw.dice_sound)

        for (die in dice) {
            die.setupDieClicks(
                dice as MutableList<Die>,
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

        Die.setupSelectionMenu(
            dice as MutableList<Die>,
            addButton,
            selectButton,
            hintText,
            totalText,
            dieMenu,
            selectMenu,
            addD4Button,
            addD6Button,
            addD8Button,
            addD10Button,
            addD12Button,
            addD20Button,
            exitSelectButton
        )

        Die.setVisibility(dice as MutableList<Die>)

//        modifierUpButton.setOnClickListener {
//            modifier++
//            if (modifier >= 0) modifierText.text =
//                "+${modifier.toString()}" else modifierText.text = modifier.toString()
//        }
//
//        modifierDownButton.setOnClickListener {
//            modifier--
//            if (modifier >= 0) modifierText.text =
//                "+${modifier.toString()}" else modifierText.text = modifier.toString()
//        }

        rollButton.setOnClickListener {
            Die.removeMenus(dice as MutableList<Die>, dieMenu, selectMenu, hintText)
            Die.resetBackground(dice as MutableList<Die>)

            // avoids infinite roll when spamming the button
            if (::timer.isInitialized) timer.cancel()

            if (isAnimationOn!!) Die.time = 8 else Die.time = 2

            val vibePattern = LongArray(2)
            vibePattern[0] = 10
            vibePattern[1] = 125 * 2

            if (dice[0].isVisible) {
                if (isVibrationOn!!) {
                    rollingBuzzer?.vibrate(VibrationEffect.createWaveform(vibePattern, -1))
                }
                if (isSoundOn!!) {
                    if (rollingSound.isPlaying) {
                        rollingSound.reset()
                        rollingSound = MediaPlayer.create(this.context, R.raw.dice_sound)
                    }
                    rollingSound.start()
                }

                timer = fixedRateTimer("timer", false, 0, 125) {
                    activity?.runOnUiThread {
                        if (Die.roll(dice as MutableList<Die>) == 0) {
                            this.cancel()
                        }
                        total = 0
                        for (die in dice) {
                            if (!die.isVisible) continue else total += die.sides.indexOf(die.recentSides.last()) + 1
                        }
//                        total += modifier
                        totalText.text = "Total: $total"
                    }
                }

                //in reality this happens before the timer because the timer runs as a coroutine in the background
                // could just put it before the timer in the code, but I'll leave it as an example.
                for (die in dice) {
                    println("before ${dice.indexOf(die)}: ${die.recentSides}")
                    die.recentSides = mutableListOf(die.recentSides.last())
                    println("after ${dice.indexOf(die)}: ${die.recentSides}")
                }
            }
        }

        selectButton.setOnClickListener {
            clSelectionPopup.visibility = View.VISIBLE
            clPopup.visibility = View.GONE
            if (!dice[0].isVisible) hintText.visibility = View.VISIBLE
        }

        addButton.setOnClickListener {
            Die.removeMenus(dice as MutableList<Die>, dieMenu, selectMenu, hintText)
            Die.resetBackground(dice as MutableList<Die>)
            for (die in dice) {
                if (!die.isVisible) {
                    Die.setVisibility(dice as MutableList<Die>)
                    die.isVisible = true
                    Die.setVisibility(dice as MutableList<Die>)
                    break
                }
            }
            Die.handleButtons(dice as MutableList<Die>, addButton, selectButton)
            totalText.text = "Total: "
        }

        clearButton.setOnClickListener {
            Die.removeMenus(dice as MutableList<Die>, dieMenu, selectMenu, hintText)
            Die.resetBackground(dice as MutableList<Die>)
            for (i in dice.indices) {
                dice[i].isVisible = false
            }
            Die.setVisibility(dice as MutableList<Die>)
            Die.handleButtons(dice as MutableList<Die>, addButton, selectButton)
            clSelectionPopup.visibility = View.VISIBLE
            hintText.visibility = View.VISIBLE
            totalText.text = "Total: "
            addButton.text = "Add "
        }

        binding.clMain.setOnClickListener {
            Die.removeMenus(dice as MutableList<Die>, dieMenu, selectMenu, hintText)
            Die.resetBackground(dice as MutableList<Die>)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        return inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Die.removeMenus(dice as MutableList<Die>, dieMenu, selectMenu, hintText)
        Die.resetBackground(dice as MutableList<Die>)

        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}