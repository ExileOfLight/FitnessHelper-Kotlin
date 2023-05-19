package com.example.fitnessfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfinal.db.FitnessDao

class FitnessViewModelFactory(private val dao: FitnessDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            return FitnessViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model Class ")
    }
}