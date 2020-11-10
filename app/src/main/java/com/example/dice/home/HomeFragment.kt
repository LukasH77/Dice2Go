package com.example.dice.home

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Layout
import android.text.Layout.Directions
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.example.dice.R
import com.example.dice.database.SettingsDatabase
import com.example.dice.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    private lateinit var timer: Timer

    private lateinit var dice: List<Die>

    private lateinit var dieMenu: ConstraintLayout
    private lateinit var selectMenu: ConstraintLayout
    private lateinit var hintText: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val database = SettingsDatabase.createInstance(this.requireActivity().application)
        val dao = database.settingsDao

        viewModelFactory = HomeViewModelFactory(dao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)

//        val settings = Settings(1, true, false)
//        dao.insert(settings)

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
//        val holdButton = binding.ibHold
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
//        val modifierText = binding.modifier
//        val modifierUpButton = binding.modifierUp
//        val modifierDownButton = binding.modifierDown
        var total = 0
        var modifier = 0

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

//        //simulate first rolls to avoid lag
//        //if database is empty or something, when dies are persistent this shouldn't happen
//        println("simulating die rolls")
//        repeat(2) {
//            if (::timer.isInitialized) timer.cancel()
//            Die.time = 3
//            timer = fixedRateTimer("timer", false, 0L, 100) {
//                activity?.runOnUiThread {
//                    if (Die.roll(dice as MutableList<Die>) == 0) {
//                        println("running")
//                        this.cancel()
//                    }
//                }
//            }
//        }

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
            viewModel.x()
            // avoids infinite roll when spamming the button
            if (::timer.isInitialized) timer.cancel()
            Die.time = 8

            val vibePattern = LongArray(2)
            vibePattern[0] = 10
            vibePattern[1] = 125 * 2

            if (dice[0].isVisible) {
                rollingBuzzer?.vibrate(VibrationEffect.createWaveform(vibePattern, -1))
                if (rollingSound.isPlaying) {
                    rollingSound.reset()
                    rollingSound = MediaPlayer.create(this.context, R.raw.dice_sound)
                }
                rollingSound.start()


                timer = fixedRateTimer("timer", false, 0, 125) {
                    activity?.runOnUiThread {
                        if (Die.roll(dice as MutableList<Die>) == 0) {
//                            println("running")
                            this.cancel()
                        }
                        for (die in dice) {
                            if (!die.isVisible) continue else total += die.sides.indexOf(die.recentSides.last()) + 1
                        }
                        total += modifier
                        totalText.text = "Total: $total"
                    }
                    total = 0
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
            val settings = viewModel.settings
            println("${settings.animation}, ${settings.sound}, ${settings.vibration}, ${settings.darkMode}")

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
        val settings = viewModel.settings

        if (item.itemId == R.id.settingsFragment) {
            println("nav")
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment(settings.animation, settings.sound, settings.vibration, settings.darkMode))
            return true
        }

        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}