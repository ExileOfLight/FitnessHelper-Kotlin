package com.example.fitnessfinal.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_dt",indices = [Index(value = ["id"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val age: Int,
    val height: Int,
    val weight: Int,
    val gender: String,
    val deficit: Double
)
