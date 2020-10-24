package com.example.dice.home

import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dice.R
import com.example.dice.databinding.FragmentHomeBinding
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var timer: Timer

    private lateinit var dice: List<Die>

    private lateinit var dieMenu: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

//        val database = SettingsDatabase.createInstance(this.requireActivity().application)
//        val dao = database.settingsDao
//        val settings = Settings(1, true, false)
//        dao.insert(settings)

        dice = mutableListOf(
            D6(binding.ivDie1),
            D6(binding.ivDie2),
            D6(binding.ivDie3),
            D6(binding.ivDie4),
            D6(binding.ivDie5),
            D6(binding.ivDie6)
        )
        dieMenu = binding.clPopup

        val replaceD4Button = binding.ibReplaceD4
        val replaceD6Button = binding.ibReplaceD6
        val replaceD8Button = binding.ibReplaceD8
        val removeButton = binding.ibRemove
        val exitButton = binding.ibExit

        val clearButton = binding.bClear
        val rollButton = binding.bRoll
        val addButton = binding.bAdd
        val totalText = binding.tvTotal
        var total = 0
        var modifier = 0

        for (die in dice) {
            die.setupDieClicks(dice as MutableList<Die>, dieMenu, replaceD4Button, replaceD6Button, replaceD8Button, removeButton, exitButton)
        }

        Die.setVisibility(dice as MutableList<Die>)

        rollButton.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice as MutableList<Die>)
            if (this::timer.isInitialized) timer.cancel()
            Die.time = 5
            timer = fixedRateTimer("timer", false, 0L, 50) {
                activity?.runOnUiThread {
                    if (Die.roll(dice as MutableList<Die>) == 0) {
                        this.cancel()
                        for (die in dice) {
                            if (!die.visibility) continue else total += die.sides.indexOf(die.recentSides.last()) + 1
                        }
                        total += modifier
                        totalText.text = "Total: $total"
                    }
                }
            }
            total = 0
        }

        addButton.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice as MutableList<Die>)
            for (die in dice) {
                if (!die.visibility) {
                    Die.setVisibility(dice as MutableList<Die>)
                    die.visibility = true
                    Die.setVisibility(dice as MutableList<Die>)
                    break
                }
            }
        }

        clearButton.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice as MutableList<Die>)
            for (i in dice.indices) {
                dice[i].visibility = false
            }
            Die.setVisibility(dice as MutableList<Die>)
        }

        binding.clMain.setOnClickListener {
            Die.removeDieMenu(dieMenu)
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
        Die.removeDieMenu(dieMenu)
        Die.resetBackground(dice as MutableList<Die>)
        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}