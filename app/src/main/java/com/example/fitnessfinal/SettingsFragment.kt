package com.example.fitnessfinal

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.fitnessfinal.databinding.FragmentSettingsBinding
import com.example.fitnessfinal.db.User
import com.example.fitnessfinal.db.UserDataBase
import kotlinx.coroutines.launch

const val deficitCONST = 0.8
const val upkeepCONST = 1.0
const val bulkCONST = 1.2
class SettingsFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    //private var current_user_id: Int = 0
    private var deficitData: Double = 0.0
    private var genderData: String = "Male"
    private var radioobserverAvailable = true




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = sharedViewModel


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dao = UserDataBase.getInstance(requireContext()).userDao()
        val factory = UserViewModelFactory(dao)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        val isMale = sharedViewModel.isMale.value!!
        val deficitData = sharedViewModel.deficitOption.value!!
        binding.radioButtonMale.isChecked = isMale
        binding.radioButtonFemale.isChecked = !isMale
        genderData = if (isMale) "Male" else "Female"
        when (deficitData) {
            deficitCONST -> {
                binding.radioButtonDeficit.isChecked = true
            }

            bulkCONST -> {
                binding.radioButtonUpkeep.isChecked = true
            }
            else -> {
                binding.radioButtonUpkeep.isChecked = true
            }
        }

        binding.genderAsk.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_male -> {
                    genderData = "Male"
                    sharedViewModel.setisMale(true)
                }

                R.id.radio_button_female -> {
                    genderData = "Female"
                    sharedViewModel.setisMale(false)
                }
            }
        }
        binding.deficitAsk.setOnCheckedChangeListener {_, checkedId ->
            when (checkedId) {
                R.id.radio_button_deficit -> {
                    sharedViewModel.setDeficit(deficitCONST)
                }

                R.id.radio_button_upkeep -> {
                    sharedViewModel.setDeficit(upkeepCONST)
                }

                R.id.radio_button_bulk -> {
                    sharedViewModel.setDeficit(bulkCONST)
                }
            }
        }
        binding.btnSubmit.setOnClickListener {
            val age = binding.edittextAge.text.toString()
            val height = binding.edittextHeight.text.toString()
            val weight = binding.edittextWeight.text.toString()

            if (inputCheck(age,height,weight,genderData)){
                sharedViewModel.updateMacros()
                sharedViewModel.insertDatatoDB(userViewModel)
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(), "Please fill out the fields", Toast.LENGTH_LONG).show()
            }

        }


    }
//

    private fun insertDatatoDB(){
        binding.apply {
            val age = edittextAge.text.toString()
            val height = edittextHeight.text.toString()
            val weight = edittextWeight.text.toString()
            if (inputCheck(age,height,weight,genderData)){
                val user = User(1, age.toInt(), height.toInt(), weight.toInt(), genderData, deficitData)
                lifecycleScope.launch {
                    userViewModel.upsertUser(user)
                }

                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(), "Please fill out the fields", Toast.LENGTH_LONG).show()
            }

        }

    }
    private fun inputCheck(age: String?,height:String?,weight:String?,gender:String): Boolean{
        return !(TextUtils.isEmpty(age) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(gender))
    }




}
