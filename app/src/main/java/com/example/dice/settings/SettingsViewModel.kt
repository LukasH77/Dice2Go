package com.example.dice.settings

import androidx.lifecycle.ViewModel
import com.example.dice.database.SettingsDao
import kotlinx.coroutines.*

class SettingsViewModel(private val dataSource: SettingsDao) : ViewModel() {
    private val job = Job()

    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun updateAnimationSettings(settings: Boolean) {
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateAnimation(settings)
            }
        }
    }

    fun updateSoundSettings(settings: Boolean) {
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateSound(settings)
            }
        }
    }

    fun updateVibrationSettings(settings: Boolean) {
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateVibration(settings)
            }
        }
    }

    fun updateThemeSettings(settings: Boolean) {
        scope.launch {
            withContext(Dispatchers.IO) {
                dataSource.updateTheme(settings)
            }
        }
    }
}