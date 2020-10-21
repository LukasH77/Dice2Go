package com.example.dice.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val animation: Boolean,
    @ColumnInfo
    val nightMode: Boolean
)