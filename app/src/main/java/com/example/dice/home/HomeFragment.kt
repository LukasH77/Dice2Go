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
            D4(binding.ivDie1),
            D6(binding.ivDie2),
            D6(binding.ivDie3),
            D6(binding.ivDie4),
            D6(binding.ivDie5),
            D6(binding.ivDie6)
        )
        dieMenu = binding.clPopup

        val replaceD4Button = binding.ibReplaceD4
        val replaceD6Button = binding.ibReplaceD6
        val removeButton = binding.ibRemove
        val exitButton = binding.ibExit

        val clearButton = binding.bClear
        val rollButton = binding.bRoll
        val addButton = binding.bAdd

        for (die in dice) {
            die.setupDieClicks(dice, dieMenu, replaceD4Button, replaceD6Button, removeButton, exitButton)
        }

        Die.setVisibility(dice)

        rollButton.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice)
            if (this::timer.isInitialized) timer.cancel()
            Die.time = 5
            timer = fixedRateTimer("timer", false, 0L, 50) {
                activity?.runOnUiThread {
                    if (Die.setImg(dice) == 0) {
                        this.cancel()
                    }
                }
            }
        }

        addButton.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice)
            for (die in dice) {
                if (!die.visibility) {
                    Die.setVisibility(dice)
                    die.visibility = true
                    Die.setVisibility(dice)
                    break
                }
            }
        }

        clearButton.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice)
            for (i in dice.indices) {
                dice[i].visibility = false
            }
            Die.setVisibility(dice)
        }

        binding.clMain.setOnClickListener {
            Die.removeDieMenu(dieMenu)
            Die.resetBackground(dice)
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
        Die.resetBackground(dice)
        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}