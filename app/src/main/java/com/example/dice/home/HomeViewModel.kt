package com.example.dice.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dice.database.Settings
import com.example.dice.database.SettingsDao
import kotlinx.coroutines.*

class HomeViewModel(private val dataSource: SettingsDao) : ViewModel() {

    private val job = Job()

    private val scope = CoroutineScope(job + Dispatchers.Main)

    var settings: Settings

    init {
        settings = getDBSettings()
//        scope.launch {
//            withContext(Dispatchers.IO) {
//                try {
//                    dataSource.insert(Settings(0, true, true, true, false))
//                    println("insert")
//                } catch (e: Exception) {
//                    dataSource.update(Settings(0, true, true, true, false))
//                    println("update")
//                }
//            }
//        }
    }

    fun x() {
//        println(_settings.value?.animation)
        println(settings.animation)
    }

    fun getDBSettings(): Settings {
        scope.launch {
            withContext(Dispatchers.IO) {
                settings =  dataSource.getSettings()
            }
        }
        return settings
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}