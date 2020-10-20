package com.example.dice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.example.dice.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var time = 0
    private val dieResources = listOf(R.drawable.d6__1, R.drawable.d6__2, R.drawable.d6__3, R.drawable.d6__4, R.drawable.d6__5, R.drawable.d6__6)
    private val recents = mutableListOf<Int>()
    private lateinit var x: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val die = binding.ivDie1

        binding.bRoll.setOnClickListener {
            if (this::x.isInitialized) x.cancel()
            time = 5
            val resultNum = (0..5).random()
            binding.tvResult.text = "Result: ${resultNum + 1}"
            x = fixedRateTimer("timer", false, 0L, 50) {
                runOnUiThread {
                    if (setImg(die, dieResources[resultNum]) == 0) {
                        this.cancel()
                    }
//                    println("running")
                }
            }
        }
    }

    private fun setImg(die: ImageView, result: Int): Int {
//        println(time)
        return if (time < 1) {
            die.setImageResource(result)
            recents.clear()
            0
        } else {
            var current = dieResources[(0..5).random()]
            recents.add(current)
            println(time)
            when (recents.size) {
                0 -> die.setImageResource(dieResources[(0..4).random()])
                1 -> while (current == recents.last()) {
                    println("Reroll: short")
                    current = dieResources[(0..5).random()]
                    die.setImageResource(dieResources[(0..5).random()])
                }
                else -> while (current == recents.last() || current == recents[recents.lastIndex - 1]) {
                    println("Reroll: medium")
                    current = dieResources[(0..5).random()]
                    die.setImageResource(dieResources[(0..5).random()])
                }
            }
            time--
            7
        }
    }
}