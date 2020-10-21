package com.example.dice.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SettingsDao {
    @Insert
    fun insert(row: Settings)
    @Update
    fun update(row: Settings)
    @Query("SELECT * FROM Settings")
    fun getAll(): List<Settings>
}