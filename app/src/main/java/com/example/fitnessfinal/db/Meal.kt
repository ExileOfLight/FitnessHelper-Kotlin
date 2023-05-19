package com.example.fitnessfinal.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_dt")
data class Meal(
    @PrimaryKey(autoGenerate = false)
    val foodName: String,
    val cals: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val isEaten: Boolean
)
