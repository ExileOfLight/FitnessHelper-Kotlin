package com.example.fitnessfinal.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "meal_dt")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    val cals: Double,
    val proteins: Double,
    val fats: Double,
    val carbs: Double,
    val amount: Double
): Parcelable
