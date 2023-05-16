package com.example.fitnessfinal

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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


class SettingsFragment : Fragment() {
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    //private var current_user_id: Int = 0
    private var deficitData: Double = 0.0
    private var genderData: String = "Male"


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

        sharedViewModel.editTextAge.observe(viewLifecycleOwner){newValue ->
            binding.edittextAge.setText(newValue)
        }
        sharedViewModel.editTextHeight.observe(viewLifecycleOwner){newValue ->
            binding.edittextHeight.setText(newValue)
        }
        sharedViewModel.editTextWeight.observe(viewLifecycleOwner){newValue ->
            binding.edittextWeight.setText(newValue)
        }
        binding.apply{
            genderAsk.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radio_button_1 -> {
                        genderData = "Male"
                        sharedViewModel.setisMale(true)
                    }
                    R.id.radio_button_2 -> {
                        genderData = "Female"
                        sharedViewModel.setisMale(false)
                    }
                }
                sharedViewModel.updateMacros()
            }
            deficitAsk.setOnCheckedChangeListener {_, checkedId ->
                when (checkedId) {
                    R.id.rectangle_button_1 -> {
                        sharedViewModel.setDeficit(300.0)
                    }
                    R.id.rectangle_button_2 -> {
                        sharedViewModel.setDeficit(0.0)
                    }
                    R.id.rectangle_button_3 -> {
                        sharedViewModel.setDeficit(-300.0)
                    }
                }
                sharedViewModel.updateMacros()
            }
            btnSubmit.setOnClickListener {
                val age = binding.edittextAge.text.toString()
                val height = binding.edittextHeight.text.toString()
                val weight = binding.edittextWeight.text.toString()

                if (inputCheck(age,height,weight,genderData)){
                    sharedViewModel.updateMacros()
                    sharedViewModel.insertDatatoDB()
                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(), "Please fill out the fields", Toast.LENGTH_LONG).show()
                }

            }
        }


    }

    override fun onPause() {
        super.onPause()
        editor.apply(){
            putString("sf_currentUser", 0.toString())
        }
    }
    /*override fun onResume() {
        super.onResume()
        sharedViewModel.loadDataFromDB(viewLifecycleOwner)
        sharedViewModel.updateMacros()
    }*/
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
