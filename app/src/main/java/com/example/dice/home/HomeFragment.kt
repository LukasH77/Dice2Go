package com.example.dice.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.dice.R
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
    private val dieResources = listOf(
        R.drawable.d6__1,
        R.drawable.d6__2,
        R.drawable.d6__3,
        R.drawable.d6__4,
        R.drawable.d6__5,
        R.drawable.d6__6
    )
    private val visibility = mutableListOf(true, false, false, false, false, false)
    private lateinit var dice: List<ImageView>

    private val recents = listOf<MutableList<Int>>(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val job = Job()

        val scope = CoroutineScope(Dispatchers.Main + job)

                dice = listOf(
            binding.ivDie1,
            binding.ivDie2,
            binding.ivDie3,
            binding.ivDie4,
            binding.ivDie5,
            binding.ivDie6
        )

        setVisibility()

        binding.bRoll.setOnClickListener {
            if (this::timer.isInitialized) timer.cancel()
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
            for ((x, state) in visibility.withIndex()) {
                if (!state) {
                    visibility[x] = true
                    setVisibility()
                    break
                }
            }
        }


        return binding.root
    }


        private fun setVisibility() {
        for ((x, die) in dice.withIndex()) {
            if (visibility[x]) {
                die.visibility = View.VISIBLE
                die.setOnClickListener {
                    binding.clPopup.visibility = View.VISIBLE
                    addOrRemove(die)
                }
            } else {
                die.visibility = View.GONE
                die.setImageResource(dieResources[5])
            }
        }
    }

    private fun addOrRemove(selectedDie: ImageView) {
        binding.ivAddD6.setOnClickListener {
            selectedDie.setImageResource(dieResources[5])
            binding.clPopup.visibility = View.GONE
        }
        binding.tvRemove.setOnClickListener {
            val currentIndex = dice.indexOf(selectedDie)
            var lastVisibleIndex = 5
            for (i in visibility.indices) {
                if (!visibility[i]) {
                    lastVisibleIndex = i - 1
                    println(lastVisibleIndex)
                    break
                }
            }
            val steps = lastVisibleIndex - currentIndex
            for (i in 0 until steps) {
                println(i)
                dice[currentIndex + i].setImageDrawable(dice[currentIndex + i + 1].drawable)
            }
            binding.clPopup.visibility = View.GONE
            visibility[lastVisibleIndex] = false
            setVisibility()
        }
        binding.bExit.setOnClickListener {
            binding.clPopup.visibility = View.GONE
        }
    }

    private fun setImg(results: List<Int>): Int {
        return if (time < 1) {
            for ((x, die) in dice.withIndex()) {
                die.setImageResource(dieResources[results[x]])
                recents[x].clear()
                setVisibility()
            }
            0
        } else {
            for (list in recents) {
                for (die in dice) {
                    var current = dieResources[(0..5).random()]
                    list.add(current)
                    when (list.size) {
                        0 -> die.setImageResource(dieResources[(0..4).random()])
                        1 -> while (current == list.last()) {
                            current = dieResources[(0..5).random()]
                            die.setImageResource(dieResources[(0..5).random()])
                        }
                        else -> while (current == list.last() || current == list[list.lastIndex - 1]) {
                            current = dieResources[(0..5).random()]
                            die.setImageResource(dieResources[(0..5).random()])
                        }
                    }
                }
            }
            println(time)
            time--
            7
        }
    }
}