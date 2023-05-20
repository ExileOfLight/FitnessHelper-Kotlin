package com.example.fitnessfinal


import android.util.Log
import androidx.lifecycle.*
import com.example.fitnessfinal.db.User
import kotlinx.coroutines.launch
import kotlin.math.round


class MainViewModel : ViewModel() {


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

    private val _deficitOption = MutableLiveData<Double>(0.0)
    val deficitOption: LiveData<Double>
        get() = _deficitOption
    fun setDeficit(deficit: Double){
        _deficitOption.value = deficit
    }




    //Update from safe args (data base parse)
    val currentProteins = MutableLiveData<String>("0")
    val currentFats = MutableLiveData<String>("0")
    val currentCarbs = MutableLiveData<String>("0")
    val currentCals = MutableLiveData<String>("0")
    val macros = MutableLiveData<Array<String>>(arrayOf("0","0","0","0"))

    fun updateMacros(){
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
        stress_factor: Double,
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
        val calories: Int = round(_calories * deficit).toInt()
        val proteins = round(_calories * protein_perct / 4).toInt()
        val fats = round(_calories * fat_perct / 8).toInt()
        val carbs = round(_calories * carb_perct / 4).toInt()
        return arrayOf(calories.toString(), proteins.toString(), fats.toString(), carbs.toString())
    }
    fun insertDatatoDB(fitnessViewModel: FitnessViewModel){
        val age = editTextAge.value!!
        val height = editTextHeight.value!!
        val weight = editTextWeight.value!!
        val gender = if (isMale.value!!) "Male" else "Female"
        val deficit = deficitOption.value!!
        val user = User(1, age.toInt(), height.toInt(), weight.toInt(), gender, deficit)
        viewModelScope.launch {
            fitnessViewModel.upsertUser(user)
        }
    }
    fun loadDataFromDB(owner:LifecycleOwner, fitnessViewModel: FitnessViewModel) {
        fitnessViewModel.users.observe(owner) { userList ->
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
