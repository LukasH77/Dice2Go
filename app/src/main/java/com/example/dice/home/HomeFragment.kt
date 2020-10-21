package com.example.dice.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dice.R
import com.example.dice.database.Settings
import com.example.dice.database.SettingsDatabase
import com.example.dice.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.concurrent.fixedRateTimer


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var timer: Timer
    private var time = 0

    private lateinit var dice: List<Die>

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



        dice = listOf(
            D6(binding.ivDie1),
            D6(binding.ivDie2),
            D6(binding.ivDie3),
            D6(binding.ivDie4),
            D6(binding.ivDie5),
            D6(binding.ivDie6)
        )

        setVisibility()

        binding.bRoll.setOnClickListener {
            if (this::timer.isInitialized) timer.cancel()
//            time = if (dao.getAll()[0].animation) 5 else 1
            time = 5
            val results = listOf(
                (0..5).random(),
                (0..5).random(),
                (0..5).random(),
                (0..5).random(),
                (0..5).random(),
                (0..5).random()
            )
            binding.tvResult.text = "Result: $results"
            timer = fixedRateTimer("timer", false, 0L, 50) {
                activity?.runOnUiThread {
                    if (setImg(results) == 0) {
                        this.cancel()
                    }
                }
            }
        }

        binding.bAdd.setOnClickListener {
            for (die in dice) {
                if (!die.visibility) {
                    die.visibility = true
                    setVisibility()
                    break
                }
            }
        }

        binding.bClear.setOnClickListener {
            for (i in dice.indices) {
                dice[i].visibility = false
            }
            setVisibility()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        return inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun setVisibility() {
        for (die in dice) {
            if (die.visibility) {
                die.uiRepresentation.visibility = View.VISIBLE
                die.uiRepresentation.setOnClickListener {
                    binding.clPopup.visibility = View.VISIBLE
                    addOrRemove(die)
                }
            } else {
                die.uiRepresentation.visibility = View.GONE
                die.uiRepresentation.setImageResource(die.initialSide)
            }
        }
    }

    private fun addOrRemove(selectedDie: Die) {
        binding.ivAddD6.setOnClickListener {
            selectedDie.uiRepresentation.setImageResource(selectedDie.initialSide)
            binding.clPopup.visibility = View.GONE
        }
        binding.tvRemove.setOnClickListener {
            val currentIndex = dice.indexOf(selectedDie)
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
            binding.clPopup.visibility = View.GONE
            dice[lastVisibleIndex].visibility = false
            setVisibility()
        }
        binding.bExit.setOnClickListener {
            binding.clPopup.visibility = View.GONE
        }
    }

    private fun setImg(results: List<Int>): Int {
        return if (time < 1) {
            for ((x, die) in dice.withIndex()) {
                die.uiRepresentation.setImageResource(die.sides[results[x]])
                die.recentSides.add(die.sides[results[x]])
                setVisibility()
            }
            0
        } else {
            for (die in dice) {
                var current = die.sides[(0..5).random()]
                val currentList = die.recentSides
                when (currentList.size) {
                    1 -> while (current == currentList.last()) {
                        current = die.sides[(0..5).random()]
                    }
                    else -> while (current == currentList.last()
                        || current == currentList[currentList.lastIndex - 1]
                        || die.sides.indexOf(current) == results[dice.indexOf(die)]
                    ) {
                        current = die.sides[(0..5).random()]
                    }
                }
                currentList.add(current)
                die.uiRepresentation.setImageResource(current)
            }
            time--
            7
        }
    }
}