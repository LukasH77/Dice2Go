package com.example.dice.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dice.database.SettingsDao

class SettingsViewModelFactory(private val dataSource: SettingsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(dataSource) as T
        }
        throw error("SettingsViewModelFactory failed")
    }
}