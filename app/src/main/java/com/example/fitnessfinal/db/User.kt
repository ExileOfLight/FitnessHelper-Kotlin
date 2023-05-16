package com.example.fitnessfinal.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_dt",indices = [Index(value = ["id"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_age")
    val age: Int,
    @ColumnInfo(name = "user_ht")
    val height: Int,
    @ColumnInfo(name = "user_wt")
    val weight: Int,
    @ColumnInfo(name = "user_gender")
    val gender: String,
    @ColumnInfo(name = "user_deficit")
    val deficit: Double
)
