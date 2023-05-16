package com.example.fitnessfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessfinal.db.User
import com.example.fitnessfinal.db.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val dao: UserDao):ViewModel() {
    val users = dao.getAllUsers()
    suspend fun upsertUser(user: User) = viewModelScope.launch {
        dao.upsertUser(user)
    }
    fun deleteUser(user: User)=viewModelScope.launch {
        dao.deleteUser(user)
    }

}