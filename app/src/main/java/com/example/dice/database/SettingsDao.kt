package com.example.dice.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface SettingsDao {
    @Insert
    fun insert(row: Settings)

    @Update
    fun update(row: Settings)

    @Query("UPDATE Settings SET animation = :change WHERE id = 0")
    fun updateAnimation(change: Boolean)

    @Query("UPDATE Settings SET sound = :change WHERE id = 0")
    fun updateSound(change: Boolean)

    @Query("UPDATE Settings SET vibration = :change WHERE id = 0")
    fun updateVibration(change: Boolean)

    @Query("UPDATE Settings SET darkMode = :change WHERE id = 0")
    fun updateTheme(change: Boolean)

    @Query("SELECT * FROM Settings WHERE id = 0")
    fun getSettings(): Settings

    @Query("DELETE FROM Settings")
    fun deleteAll()
}