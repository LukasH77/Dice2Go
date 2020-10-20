package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.example.dice.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: Timer

    private var time = 0
    private val dieResources = listOf(R.drawable.d6__1, R.drawable.d6__2, R.drawable.d6__3, R.drawable.d6__4, R.drawable.d6__5, R.drawable.d6__6)
    private val visibility = mutableListOf(true, false, false, false, false, false)
    private lateinit var dice: List<ImageView>
    private lateinit var clickedDie: ImageView

    private val recents = listOf<MutableList<Int>>(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        dice = listOf(binding.ivDie1, binding.ivDie2, binding.ivDie3, binding.ivDie4, binding.ivDie5, binding.ivDie6)

        setVisibility()

        binding.bRoll.setOnClickListener {
            if (this::timer.isInitialized) timer.cancel()
            time = 5
            val results = listOf((0..5).random(), (0..5).random(), (0..5).random(), (0..5).random(), (0..5).random(), (0..5).random())
            binding.tvResult.text = "Results: $results"
            timer = fixedRateTimer("timer", false, 0L, 50) {
                runOnUiThread {
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
    }

    private fun setVisibility() {
        for ((x, die) in dice.withIndex()) {
            if (visibility[x]) {
                die.setImageResource(dieResources[5])
                die.visibility = View.VISIBLE
                die.setOnClickListener {
                    binding.clPopup.visibility = View.VISIBLE
                    addOrRemove(die)
                }
            } else die.visibility = View.GONE
        }
    }

    private fun addOrRemove(selectedDie: ImageView) {
        binding.ivAddD6.setOnClickListener {
            selectedDie.setImageResource(dieResources[5])
            binding.clPopup.visibility = View.GONE
        }
        binding.tvRemove.setOnClickListener {
            visibility[dice.indexOf(selectedDie)] = false
            binding.clPopup.visibility = View.GONE
            setVisibility()
        }
    }

    private fun setImg(results: List<Int>): Int {
        return if (time < 1) {
            for ((x, die) in dice.withIndex()) {
                die.setImageResource(dieResources[results[x]])
                recents[x].clear()
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