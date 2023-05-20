package com.example.fitnessfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfinal.db.FitnessDao
import com.example.fitnessfinal.db.FitnessRepository

class FitnessViewModelFactory(private val repository: FitnessRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            return FitnessViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class ")
    }
}