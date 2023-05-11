package com.example.fitnessfinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//dksmdksmdskm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.round

class MainViewModel : ViewModel() {
    //TODO: Make defaults (using Room)
    val editTextAge = MutableLiveData<String>("20")
        fun updateEditTextAge(text: String) {
            editTextAge.value = text
        }
    val editTextHeight = MutableLiveData<String>("180")
    val editTextWeight = MutableLiveData<String>("78")
    private val _isMale = MutableLiveData<Boolean>(true)
    val isMale : LiveData<Boolean>
        get() = _isMale
    fun setisMale (male: Boolean){
        _isMale.value = male
    }




    //Update from database
    private val _showProteins = MutableLiveData<String>("1")
    val showProteins: LiveData<String> = _showProteins
    private val _showFats = MutableLiveData<String>("2")
    val showFats: LiveData<String> = _showFats
    private val _showCarbs = MutableLiveData<String>("3")
    val showCarbs: LiveData<String> = _showCarbs
    private val _showCals = MutableLiveData<String>("4")
    val showCals: LiveData<String> = _showCals
    val macros = MutableLiveData<Array<String>>(arrayOf("0","0","0","0"))
    fun updateMacros():Unit{
        val newMacros = calculate_macros(editTextAge.value!!.toInt(),
            editTextHeight.value!!.toInt(),
            editTextWeight.value!!.toInt(),
            true,1.0, arrayOf(0.25,0.3,0.45,0.0))
        macros.value = newMacros
    }

    fun calculate_macros(age: Int,
                         height: Int,
                         weight: Int,
                         male: Boolean,
                         stress_factor: Double = 1.0,
                         diet_plan: Array<Double>)
    : Array<String>{
        val protein_perct = diet_plan[0]
        val fat_perct = diet_plan[1]
        val carb_perct = diet_plan[2]
        val deficit = diet_plan[3]
        var _calories: Double
        _calories = if (male){
            5 + (10 * weight) + (6.25 * height) - (5 * age)
        }else {
            -161 + (10 * weight) + (6.25 * height) - (5 * age)
        }
        _calories *= stress_factor
        val calories: Int = (round(_calories) - deficit).toInt()
        val proteins = round(_calories * protein_perct / 4).toInt()
        val fats = round(_calories * fat_perct / 8).toInt()
        val carbs = round(_calories * carb_perct / 4).toInt()
        val resultArray: Array<String> = arrayOf(calories.toString(),proteins.toString(),fats.toString(),carbs.toString())
        return resultArray
    }
}