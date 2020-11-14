package com.example.dice.home
//
//import android.os.Build
//import android.widget.ImageView
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import com.example.dice.database.Settings
//import com.example.dice.database.SettingsDao
//import kotlinx.coroutines.*
//
//class HomeViewModel(
//    private val dataSource: SettingsDao,
//    private val d1: ImageView,
//    private val d2: ImageView,
//    private val d3: ImageView,
//    private val d4: ImageView,
//    private val d5: ImageView,
//    private val d6: ImageView
//) : ViewModel() {
//
//    private val job = Job()
//
//    private val scope = CoroutineScope(job + Dispatchers.Main)
//
//    var settings: Settings
//
//    val dice = mutableListOf(
//        D4(d1),
//        D6(d2),
//        D8(d3),
//        D10(d4),
//        D12(d5),
//        D20(d6)
//    )
//
//    init {
//        settings = getDBSettings()
//        scope.launch {
//            withContext(Dispatchers.IO) {
//                try {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        dataSource.insert(Settings(0, true, true, true, false))
//                    } else {
//                        dataSource.insert(Settings(0, true, true, false, false))
//                    }
//                    println("insert")
//                } catch (e: Exception) {
//                    println(e.message)
//                }
//            }
//        }
//    }
//
//    fun x() {
////        println(_settings.value?.animation)
//        println(settings.animation)
//    }
//
//    fun getDBSettings(): Settings {
//        scope.async {
//            withContext(Dispatchers.IO) {
//                settings = dataSource.getSettings()
//            }
//        }
//        return settings
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        job.cancel()
//    }
//}