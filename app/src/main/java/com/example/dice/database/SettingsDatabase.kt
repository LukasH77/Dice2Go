package com.example.dice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Settings::class], version = 2)
abstract class SettingsDatabase : RoomDatabase() {

    abstract val settingsDao: SettingsDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: SettingsDatabase

        fun createInstance(context: Context): SettingsDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SettingsDatabase::class.java,
                        "Settings",
                    )
//                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}