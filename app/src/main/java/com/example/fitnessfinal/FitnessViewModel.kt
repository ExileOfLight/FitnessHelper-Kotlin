package com.example.fitnessfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfinal.db.User
import com.example.fitnessfinal.db.FitnessDao
import com.example.fitnessfinal.db.Meal
import kotlinx.coroutines.launch

class FitnessViewModel(private val dao: FitnessDao):ViewModel() {
    val users = dao.getAllUsers()
    val meals: LiveData<List<Meal>> = dao.getAllMeals()
    suspend fun upsertUser(user: User) = viewModelScope.launch {
        dao.upsertUser(user)
    }
    fun deleteUser(user: User)=viewModelScope.launch {
        dao.deleteUser(user)
    }
    suspend fun upsertMeal(meal: Meal) = viewModelScope.launch {
        dao.upsertMeal(meal)
    }

    fun deleteMeal(meal: Meal)=viewModelScope.launch {
        dao.deleteMeal(meal)
    }
    fun deleteMealById(id: Long)=viewModelScope.launch {
        dao.deleteMealById(id)
    }
}