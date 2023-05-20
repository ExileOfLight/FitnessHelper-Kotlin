package com.example.fitnessfinal.recyclerViewHandlers

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnessfinal.CurrentDataManager
import com.example.fitnessfinal.FitnessViewModel
import com.example.fitnessfinal.FitnessViewModelFactory
import com.example.fitnessfinal.MainViewModel
import com.example.fitnessfinal.R
import com.example.fitnessfinal.databinding.FragmentUpdateBinding
import com.example.fitnessfinal.db.FitnessDataBase
import com.example.fitnessfinal.db.FitnessRepository
import com.example.fitnessfinal.db.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: MainViewModel by activityViewModels()
    private lateinit var fitnessViewModel: FitnessViewModel
    lateinit var currentDataManager: CurrentDataManager
    private lateinit var updateJob: Job




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = FitnessDataBase.getInstance(requireContext()).fitnessDao()
        val factory = FitnessViewModelFactory(FitnessRepository(dao))
        fitnessViewModel = ViewModelProvider(this, factory)[FitnessViewModel::class.java]
        currentDataManager = CurrentDataManager(requireContext())

        binding.updateTextName.setText(args.currentMeal.foodName)
        binding.updateTextCals.setText(args.currentMeal.cals.toString())
        binding.updateTextProteins.setText(args.currentMeal.proteins.toString())
        binding.updateTextFats.setText(args.currentMeal.fats.toString())
        binding.updateTextCarbs.setText(args.currentMeal.carbs.toString())
        binding.updateTextAmount.setText(args.currentMeal.amount.toString())
        binding.updateBtn.setOnClickListener{
            updateItem()
        }
        binding.deleteBtn.setOnClickListener {
            deleteItem()
        }






    }
    private fun updateItem(){
        val mealName = binding.updateTextName.text.toString()
        val cals = binding.updateTextCals.text.toString()
        val proteins = binding.updateTextProteins.text.toString()
        val fats = binding.updateTextFats.text.toString()
        val carbs = binding.updateTextCarbs.text.toString()
        val currentAmount = binding.updateTextAmount.text.toString()

        if(inputCheck(mealName, cals, proteins,fats,carbs,currentAmount)){
            val meal = Meal(
                args.currentMeal.id,
                mealName,
                cals.toDouble(),
                proteins.toDouble(),
                fats.toDouble(),
                carbs.toDouble(),
                currentAmount.toInt()
            )
            lifecycleScope.launch {
                fitnessViewModel.upsertMeal(meal)
            }
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            val oldAmount = args.currentMeal.amount
            val newAmount = currentAmount.toInt()
            var viewmodelCals = sharedViewModel.currentCals.value?.toDouble()!!
            var viewmodelProteins = sharedViewModel.currentProteins.value?.toDouble()!!
            var viewmodelFats = sharedViewModel.currentFats.value?.toDouble()!!
            var viewmodelCarbs = sharedViewModel.currentCarbs.value?.toDouble()!!
            if (oldAmount!=0) {
                viewmodelCals -= args.currentMeal.cals * oldAmount
                viewmodelProteins -= args.currentMeal.proteins * oldAmount
                viewmodelFats -= args.currentMeal.fats * oldAmount
                viewmodelCarbs -= args.currentMeal.carbs * oldAmount
            }
            if (newAmount!=0) {
                viewmodelCals += cals.toDouble() * newAmount
                viewmodelProteins += proteins.toDouble() * newAmount
                viewmodelFats += fats.toDouble() * newAmount
                viewmodelCarbs += carbs.toDouble() * newAmount
            }
            updateJob = CoroutineScope(Dispatchers.Main).launch {
                currentDataManager.storeCurrents(viewmodelCals,viewmodelProteins,viewmodelFats,viewmodelCarbs)
            }
            findNavController().navigate(R.id.action_updateFragment_to_foodFragment)


        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, cals: String, prots: String, fats: String, carbs: String,currentAmount: String):Boolean{
        return !(TextUtils.isEmpty(name) &&
                TextUtils.isEmpty(cals) &&
                TextUtils.isEmpty(prots) &&
                TextUtils.isEmpty(fats) &&
                TextUtils.isEmpty(carbs) &&
                TextUtils.isEmpty(currentAmount))
    }
    private fun deleteItem(){
        var viewmodelCals = sharedViewModel.currentCals.value?.toDouble()!!
        var viewmodelProteins = sharedViewModel.currentProteins.value?.toDouble()!!
        var viewmodelFats = sharedViewModel.currentFats.value?.toDouble()!!
        var viewmodelCarbs = sharedViewModel.currentCarbs.value?.toDouble()!!
        var amount = args.currentMeal.amount
        if (amount!=0) {
            viewmodelCals -= args.currentMeal.cals * amount
            viewmodelProteins -= args.currentMeal.proteins * amount
            viewmodelFats -= args.currentMeal.fats * amount
            viewmodelCarbs -= args.currentMeal.carbs * amount
        }
        updateJob = CoroutineScope(Dispatchers.Main).launch {
            currentDataManager.storeCurrents(viewmodelCals,viewmodelProteins,viewmodelFats,viewmodelCarbs)
        }
        fitnessViewModel.deleteMealById(args.currentMeal.id)
        findNavController().navigate(R.id.action_updateFragment_to_foodFragment)
    }
//    override fun onDestroy() {
//        super.onDestroy()
//        if (updateJob.isActive) {
//            updateJob.cancel()
//        }
//    }

}
