package com.example.fitnessfinal


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.fitnessfinal.db.User
import com.example.fitnessfinal.db.UserDataBase
import kotlinx.coroutines.launch
import kotlin.math.round


class MainViewModel() : ViewModel() {


    //TODO: Make defaults (using Room)
    val editTextAge = MutableLiveData<String>("0")
    val editTextHeight = MutableLiveData<String>("0")
    val editTextWeight = MutableLiveData<String>("0")

    private val _isMale = MutableLiveData<Boolean>(true)
    val isMale : LiveData<Boolean>
        get() = _isMale
    fun setisMale (isMale: Boolean){
        _isMale.value = isMale
    }

    private val _deficitOption = MutableLiveData<Double>(1000.0)
    val deficitOption: LiveData<Double>
        get() = _deficitOption
    fun setDeficit(deficit: Double){
        _deficitOption.value = deficit
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
        Log.e("DEBUG", "UPDATED")
        val newMacros = calculate_macros(editTextAge.value!!.toInt(),
            editTextHeight.value!!.toInt(),
            editTextWeight.value!!.toInt(),
            isMale.value!!,1.0, arrayOf(0.25,0.3,0.45,deficitOption.value!!))
        macros.value = newMacros
    }

    fun calculate_macros(
        age: Int,
        height: Int,
        weight: Int,
        male: Boolean,
        stress_factor: Double = 1.0,
        diet_plan: Array<Double>
    )
            : Array<String> {
        val protein_perct = diet_plan[0]
        val fat_perct = diet_plan[1]
        val carb_perct = diet_plan[2]
        val deficit = diet_plan[3]
        var _calories: Double
        _calories = if (male) {
            5 + (10 * weight) + (6.25 * height) - (5 * age)
        } else {
            -161 + (10 * weight) + (6.25 * height) - (5 * age)
        }
        _calories *= stress_factor
        val calories: Int = (round(_calories) - deficit).toInt()
        val proteins = round(_calories * protein_perct / 4).toInt()
        val fats = round(_calories * fat_perct / 8).toInt()
        val carbs = round(_calories * carb_perct / 4).toInt()
        return arrayOf(calories.toString(), proteins.toString(), fats.toString(), carbs.toString())
    }
    fun insertDatatoDB(userViewModel: UserViewModel){
        val age = editTextAge.value!!
        val height = editTextHeight.value!!
        val weight = editTextWeight.value!!
        val gender = if (isMale.value!!) "Male" else "Female"
        val deficit = deficitOption.value!!
        val user = User(1, age.toInt(), height.toInt(), weight.toInt(), gender, deficit)
        viewModelScope.launch {
            userViewModel.upsertUser(user)
        }
    }
    fun loadDataFromDB(owner:LifecycleOwner, userViewModel: UserViewModel) {
        userViewModel.users.observe(owner) { userList ->
            if (userList != null && userList.isNotEmpty()) {
                //Log.e("DEBUG", "WWWWWWWWWWWWWWWWWWWWW")
                val currentUser = userList[0]
                editTextAge.value = currentUser.age.toString()
                editTextHeight.value = currentUser.height.toString()
                editTextWeight.value = currentUser.weight.toString()
                setisMale(userList[0].gender == "Male")
                setDeficit(userList[0].deficit)
            }
        }
    }



}
