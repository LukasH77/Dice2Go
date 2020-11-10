package com.example.dice.home

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dice.database.SettingsDao

class HomeViewModelFactory(private val dataSource: SettingsDao,
                           private val d1: ImageView,
                           private val d2: ImageView,
                           private val d3: ImageView,
                           private val d4: ImageView,
                           private val d5: ImageView,
                           private val d6: ImageView
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource, d1, d2, d3, d4, d5, d6) as T
        }
        throw error("HomeViewModelFactory failed")
    }
}