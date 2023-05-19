package com.example.fitnessfinal.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitnessfinal.FitnessViewModel
import com.example.fitnessfinal.MainViewModel
import com.example.fitnessfinal.R
import com.example.fitnessfinal.databinding.FragmentAddBinding
import com.example.fitnessfinal.db.Meal
import kotlinx.coroutines.launch
//TODO: FIX NAVIGATION
class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var fitnessViewModel: FitnessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun insertDataToDatabase() {
        val mealName = binding.edittextName.text.toString()
        val cals = binding.edittextCals.text.toString()
        val proteins = binding.edittextProteins.text.toString()
        val fats = binding.edittextFats.text.toString()
        val carbs = binding.edittextCarbs.text.toString()

        if(inputCheck(mealName, cals, proteins,fats,carbs)){
            val meal = Meal(
                mealName,
                cals.toInt(),
                proteins.toInt(),
                fats.toInt(),
                carbs.toInt(),
                false
            )
            lifecycleScope.launch {
                fitnessViewModel.upsertMeal(meal)
            }
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_foodFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, cals: String, prots: String, fats: String, carbs: String):Boolean{
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(cals) && TextUtils.isEmpty(prots) && TextUtils.isEmpty(fats) && TextUtils.isEmpty(carbs))
    }

}