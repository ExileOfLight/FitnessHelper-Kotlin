package com.example.fitnessfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfinal.db.User
import com.example.fitnessfinal.db.FitnessRepository
import com.example.fitnessfinal.db.Meal
import kotlinx.coroutines.launch

class FitnessViewModel(private val repository: FitnessRepository):ViewModel() {
    val users = repository.getAllUsers()
    val meals = repository.getAllMeals()
    suspend fun upsertUser(user: User) = viewModelScope.launch {
        repository.upsertUser(user)
    }
    fun deleteUser(user: User)=viewModelScope.launch {
        repository.deleteUser(user)
    }
    suspend fun upsertMeal(meal: Meal) = viewModelScope.launch {
        repository.upsertMeal(meal)
    }

    fun deleteMeal(meal: Meal)=viewModelScope.launch {
        repository.deleteMeal(meal)
    }
    fun deleteMealById(id: Long)=viewModelScope.launch {
        repository.deleteMealById(id)
    }
}