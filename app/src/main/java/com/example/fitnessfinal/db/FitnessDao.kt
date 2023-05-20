package com.example.fitnessfinal.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FitnessDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Upsert
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteUser(user: User)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("DELETE FROM meal_dt WHERE id = :mealId")
    suspend fun deleteMealById(mealId: Long)

    @Query("SELECT * FROM user_dt")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM meal_dt")
    fun getAllMeals(): LiveData<List<Meal>>

}