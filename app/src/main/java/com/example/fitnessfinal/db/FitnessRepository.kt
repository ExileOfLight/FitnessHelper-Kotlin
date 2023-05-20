package com.example.fitnessfinal.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FitnessRepository(private val dao: FitnessDao) {
    suspend fun upsertUser(user: User) {
        withContext(Dispatchers.IO) {
            dao.upsertUser(user)
        }
    }

    suspend fun upsertMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            dao.upsertMeal(meal)
        }
    }

    suspend fun deleteUser(user: User) {
        withContext(Dispatchers.IO) {
            dao.deleteUser(user)
        }
    }
    suspend fun deleteMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            dao.deleteMeal(meal)
        }
    }
    suspend fun deleteMealById(id: Long){
        withContext(Dispatchers.IO){
            dao.deleteMealById(id)
        }
    }

    fun getAllUsers(): LiveData<List<User>> {
        return  dao.getAllUsers()
    }
    fun getAllMeals(): LiveData<List<Meal>> {
        return dao.getAllMeals()
    }
}